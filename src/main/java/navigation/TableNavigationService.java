package navigation;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目级表导航索引：扫描项目内所有 .hql / .sql 文件，
 * 建立两类映射：
 *  1) 表名 -> CREATE TABLE 定义位置（文件 + 表名 token 偏移）
 *  2) 表名 -> 所有 INSERT INTO 该表的语句位置
 *
 * 采用懒加载 + 基于文件修改戳的失效策略，避免每次解析都全量扫描。
 */
public final class TableNavigationService {

    private final Project project;

    private final Map<String, DefLocation> defCache = new HashMap<>();
    private final Map<String, List<InsertHit>> insertCache = new HashMap<>();
    private final Map<VirtualFile, Long> scannedStamps = new HashMap<>();
    private volatile boolean built = false;

    // 表名：每段可选反引号，段间用 . 连接（兼容 Doris 的 `db`.`tbl` 写法）
    private static final String TABLE_NAME = "(`?[\\w]+`?(?:\\.`?[\\w]+`?)*)";
    // CREATE TABLE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [`db`.]`table`
    private static final Pattern CREATE_TABLE = Pattern.compile(
            "(?i)CREATE\\s+(?:TEMPORARY\\s+)?(?:EXTERNAL\\s+)?TABLE\\s+(?:IF\\s+NOT\\s+EXISTS\\s+)?" + TABLE_NAME);
    // INSERT [OVERWRITE|INTO] [TABLE] [`db`.]`table`（兼容 INSERT INTO TABLE t / INSERT OVERWRITE TABLE t）
    private static final Pattern INSERT_INTO = Pattern.compile(
            "(?i)INSERT\\s+(?:OVERWRITE\\s+|INTO\\s+)?(?:TABLE\\s+)?" + TABLE_NAME);

    public TableNavigationService(Project project) {
        this.project = project;
    }

    public static TableNavigationService getInstance(@NotNull Project project) {
        return project.getService(TableNavigationService.class);
    }

    /** 一条 INSERT INTO 命中的信息 */
    public static final class InsertHit {
        public final VirtualFile file;
        public final int offset;
        public final String path;
        public final int line;
        public final String statement;

        public InsertHit(VirtualFile file, int offset, String path, int line, String statement) {
            this.file = file;
            this.offset = offset;
            this.path = path;
            this.line = line;
            this.statement = statement;
        }

        @Override
        public String toString() {
            return path + " : " + line;
        }
    }

    private static final class DefLocation {
        final VirtualFile file;
        final int offset;

        DefLocation(VirtualFile f, int o) {
            this.file = f;
            this.offset = o;
        }
    }

    private void ensureBuilt() {
        if (built && !isStale()) {
            return;
        }
        synchronized (this) {
            if (built && !isStale()) {
                return;
            }
            rebuild();
        }
    }

    private boolean isStale() {
        for (Map.Entry<VirtualFile, Long> e : scannedStamps.entrySet()) {
            if (e.getKey().getModificationStamp() != e.getValue().longValue()) {
                return true;
            }
        }
        return false;
    }

    private void rebuild() {
        defCache.clear();
        insertCache.clear();
        scannedStamps.clear();
        long start = System.currentTimeMillis();
        // 时间预算：慢盘/超大项目下避免在 EDT 上长时间阻塞；超出后保留已建的部分索引
        final long budgetMs = 3000L;
        for (VirtualFile vf : collectSqlFiles()) {
            if (System.currentTimeMillis() - start > budgetMs) {
                break;
            }
            if (!vf.isValid()) {
                continue;
            }
            scannedStamps.put(vf, vf.getModificationStamp());
            String text = readText(vf);
            if (text == null) {
                continue;
            }
            indexDefinitions(vf, text);
            indexInserts(vf, text);
        }
        built = true;
    }

    private void indexDefinitions(VirtualFile vf, String text) {
        Matcher m = CREATE_TABLE.matcher(text);
        while (m.find()) {
            String name = stripBackticks(m.group(1));
            int offset = m.start(1);
            DefLocation loc = new DefLocation(vf, offset);
            defCache.putIfAbsent(name, loc);
            String simple = simpleName(name);
            if (simple != null) {
                defCache.putIfAbsent(simple, loc);
            }
        }
    }

    private void indexInserts(VirtualFile vf, String text) {
        Matcher m = INSERT_INTO.matcher(text);
        while (m.find()) {
            String name = stripBackticks(m.group(1));
            int offset = m.start(1);
            String stmt = extractStatement(text, m.start());
            int line = lineOf(vf, offset);
            InsertHit hit = new InsertHit(vf, offset, vf.getPath(), line, stmt);
            insertCache.computeIfAbsent(name, k -> new ArrayList<>()).add(hit);
            String simple = simpleName(name);
            if (simple != null) {
                insertCache.computeIfAbsent(simple, k -> new ArrayList<>()).add(hit);
            }
        }
    }

    /**
     * 解析表名的跳转目标，按以下优先级：
     *  1) CREATE TABLE 定义语句
     *  2) 文件名匹配：项目内存在 &lt;表名&gt;.sql / &lt;表名&gt;.hql，视为该表的“归属文件”
     *  3) INSERT INTO 回退：跳到写入该表的文件（取首个命中）
     * 都找不到返回 null。
     */
    @Nullable
    public PsiElement findDefinition(@NotNull String tableName) {
        ensureBuilt();
        tableName = stripBackticks(tableName);

        // 1) CREATE TABLE 定义
        DefLocation loc = defCache.get(tableName);
        if (loc == null) {
            loc = matchDefBySuffix(tableName);
        }
        if (loc != null) {
            return psiAt(loc.file, loc.offset);
        }

        // 2) 文件名匹配：<表名>.sql / <表名>.hql 即视为该表的归属文件
        String simple = simpleName(tableName);
        if (simple != null) {
            PsiElement byFile = findByFilename(simple);
            if (byFile != null) {
                return byFile;
            }
        }

        // 3) INSERT INTO 回退：跳到写入该表的文件
        List<InsertHit> inserts = findInsertIntos(tableName);
        if (!inserts.isEmpty()) {
            InsertHit hit = inserts.get(0);
            return psiAt(hit.file, hit.offset);
        }

        return null;
    }

    /** 仅查找 CREATE TABLE 定义位置（不含文件名/INSERT 回退） */
    @Nullable
    public PsiElement findCreateTableDefinition(@NotNull String tableName) {
        ensureBuilt();
        tableName = stripBackticks(tableName);
        DefLocation loc = defCache.get(tableName);
        if (loc == null) {
            loc = matchDefBySuffix(tableName);
        }
        return loc == null ? null : psiAt(loc.file, loc.offset);
    }

    /** 在项目已扫描文件中查找文件名（不含扩展名）恰好等于表名的文件，优先落在表名首次出现处 */
    @Nullable
    private PsiElement findByFilename(@NotNull String simpleTableName) {
        for (VirtualFile vf : scannedStamps.keySet()) {
            if (vf.getNameWithoutExtension().equalsIgnoreCase(simpleTableName)) {
                int offset = 0;
                String text = readText(vf);
                if (text != null) {
                    Pattern p = Pattern.compile("(?i)\\b" + Pattern.quote(simpleTableName) + "\\b");
                    Matcher m = p.matcher(text);
                    if (m.find()) {
                        offset = m.start();
                    }
                }
                return psiAt(vf, offset);
            }
        }
        return null;
    }

    /** 收集所有写入该表的 INSERT 语句 */
    @NotNull
    public List<InsertHit> findInsertIntos(@NotNull String tableName) {
        ensureBuilt();
        tableName = stripBackticks(tableName);
        List<InsertHit> list = insertCache.get(tableName);
        if (list == null) {
            list = matchInsertBySuffix(tableName);
        }
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 快速路径：按文件名（不含扩展名）精确匹配 <表名>.sql / <表名>.hql，
     * 仅读取命中文件，不做全项目扫描（大项目首点不会卡 UI）。
     * 文件内优先定位 INSERT INTO 此表的位置；没有 INSERT 则落在表名首次出现处。
     */
    @Nullable
    public InsertHit findSameNamedFile(@NotNull String simpleTableName) {
        String name = stripBackticks(simpleTableName);
        for (VirtualFile vf : filesByBaseName(name)) {
            if (!vf.isValid()) {
                continue;
            }
            String text = readText(vf);
            if (text == null) {
                continue;
            }
            // 优先：该文件里 INSERT INTO 此表的位置
            Matcher ins = INSERT_INTO.matcher(text);
            while (ins.find()) {
                String hitName = stripBackticks(ins.group(1));
                if (hitName.equalsIgnoreCase(name) || hitName.endsWith("." + name)) {
                    int off = ins.start(1);
                    return new InsertHit(vf, off, vf.getPath(), lineOf(vf, off), "");
                }
            }
            // 回退：表名首次出现处
            Matcher t = Pattern.compile("(?i)\\b" + Pattern.quote(name) + "\\b").matcher(text);
            int off = t.find() ? t.start() : 0;
            return new InsertHit(vf, off, vf.getPath(), lineOf(vf, off), "");
        }
        return null;
    }

    /** 用文件名索引找 <表名>.sql / <表名>.hql（只查文件名，不读内容、不做全量扫描） */
    private List<VirtualFile> filesByBaseName(String baseName) {
        List<VirtualFile> out = new ArrayList<>();
        try {
            Collection<VirtualFile> sqls = FilenameIndex.getAllFilesByExt(project, "sql");
            for (VirtualFile vf : sqls) {
                if (vf.getNameWithoutExtension().equalsIgnoreCase(baseName)) {
                    out.add(vf);
                }
            }
            Collection<VirtualFile> hqls = FilenameIndex.getAllFilesByExt(project, "hql");
            for (VirtualFile vf : hqls) {
                if (vf.getNameWithoutExtension().equalsIgnoreCase(baseName)) {
                    out.add(vf);
                }
            }
        } catch (Exception ignored) {
            // 索引不可用（如 dumb mode）时返回空，调用方走其它回退
        }
        return out;
    }

    /**
     * 纯正则：从光标处提取表名，不依赖 PSI 节点类型。
     * 允许 库.表 与反引号写法；返回最后一段（库.表 -> 表）。
     */
    @Nullable
    public static String extractTableName(@NotNull Editor editor) {
        Document doc = editor.getDocument();
        String text = doc.getText();
        int offset = editor.getCaretModel().getOffset();
        if (offset < 0 || offset > text.length()) {
            return null;
        }
        int start = offset;
        int end = offset;
        while (start > 0 && isTableChar(text.charAt(start - 1))) {
            start--;
        }
        while (end < text.length() && isTableChar(text.charAt(end))) {
            end++;
        }
        if (start == end) {
            return null;
        }
        String raw = text.substring(start, end);
        String cleaned = raw.replace("`", "");
        int dot = cleaned.lastIndexOf('.');
        String name = dot >= 0 ? cleaned.substring(dot + 1) : cleaned;
        name = name.trim();
        return name.isEmpty() ? null : name;
    }

    private static boolean isTableChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '`';
    }

    @Nullable
    private DefLocation matchDefBySuffix(String tableName) {
        for (Map.Entry<String, DefLocation> e : defCache.entrySet()) {
            if (e.getKey().endsWith("." + tableName)) {
                return e.getValue();
            }
        }
        return null;
    }

    @Nullable
    private List<InsertHit> matchInsertBySuffix(String tableName) {
        for (Map.Entry<String, List<InsertHit>> e : insertCache.entrySet()) {
            if (e.getKey().endsWith("." + tableName)) {
                return e.getValue();
            }
        }
        return null;
    }

    @Nullable
    PsiElement psiAt(VirtualFile vf, int offset) {
        PsiFile pf = PsiManager.getInstance(project).findFile(vf);
        if (pf == null) {
            return null;
        }
        return pf.findElementAt(offset);
    }

    private int lineOf(VirtualFile vf, int offset) {
        try {
            com.intellij.openapi.editor.Document doc =
                    com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getDocument(vf);
            if (doc != null) {
                return doc.getLineNumber(offset) + 1;
            }
        } catch (Exception ignored) {
            // 文件未打开时回退到行号 1
        }
        return 1;
    }

    private static String extractStatement(String text, int start) {
        int end = text.indexOf(';', start);
        if (end < 0) {
            end = Math.min(text.length(), start + 600);
        }
        return text.substring(start, end).trim();
    }

    private static String stripBackticks(String s) {
        return s.replace("`", "");
    }

    @Nullable
    private static String simpleName(String qualified) {
        int dot = qualified.lastIndexOf('.');
        return dot >= 0 ? qualified.substring(dot + 1) : null;
    }

    @Nullable
    private String readText(VirtualFile vf) {
        try {
            return new String(vf.contentsToByteArray(), vf.getCharset());
        } catch (Exception e) {
            return null;
        }
    }

    private List<VirtualFile> collectSqlFiles() {
        Set<VirtualFile> result = new LinkedHashSet<>();
        // 1) 项目所有内容根：多模块/多根项目才能全覆盖
        try {
            for (VirtualFile root : ProjectRootManager.getInstance(project).getContentRoots()) {
                collectSqlFiles(root, result);
            }
        } catch (Exception ignored) {
            // 极端场景取不到 content roots，下面用 baseDir 兜底
        }
        // 2) 兜底：项目根目录（content roots 为空时仍能扫到）
        VirtualFile base = project.getBaseDir();
        if (base != null) {
            collectSqlFiles(base, result);
        }
        return new ArrayList<>(result);
    }

    private void collectSqlFiles(VirtualFile dir, Set<VirtualFile> out) {
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        String name = dir.getName();
        if (name.equals(".git") || name.equals("build") || name.equals("target")
                || name.equals("node_modules") || name.equals(".idea")
                || name.equals(".gradle") || name.equals(".intellijPlatform")
                || name.equals("out") || name.equals(".settings")) {
            return;
        }
        for (VirtualFile child : dir.getChildren()) {
            if (child.isDirectory()) {
                collectSqlFiles(child, out);
            } else {
                String ext = child.getExtension();
                if (ext != null && (ext.equalsIgnoreCase("hql") || ext.equalsIgnoreCase("sql"))) {
                    out.add(child);
                }
            }
        }
    }
}
