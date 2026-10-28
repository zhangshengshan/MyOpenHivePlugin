package navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 让 Ctrl+Click / Ctrl+B（跳转到声明）走纯正则表跳转逻辑：
 * 从光标处用正则提取表名，扫描项目找到“包含该表名且含 insert into 表名”的文件并跳转。
 *
 * 只作用于 .sql / .hql 文件，避免影响 Java 等其它语言的 Ctrl+Click。
 */
public class TableGotoDeclarationHandler implements GotoDeclarationHandler {

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (editor == null) {
            return null;
        }
        // 仅对 .sql / .hql 生效，其它语言交给平台默认逻辑
        VirtualFile vf = editor.getVirtualFile();
        if (vf == null) {
            return null;
        }
        String ext = vf.getExtension();
        if (ext == null || (!ext.equalsIgnoreCase("sql") && !ext.equalsIgnoreCase("hql"))) {
            return null;
        }

        String tableName = TableNavigationService.extractTableName(editor);
        if (tableName == null || tableName.isEmpty()) {
            return null;
        }
        Project project = editor.getProject();
        if (project == null) {
            return null;
        }

        TableNavigationService service = TableNavigationService.getInstance(project);
        List<PsiElement> targets = new ArrayList<>();

        // 1) 快路径：文件名精确等于表名的文件（<表名>.sql/.hql），只读命中文件，不做全项目扫描
        TableNavigationService.InsertHit byFile = service.findSameNamedFile(tableName);
        if (byFile != null) {
            PsiElement el = service.psiAt(byFile.file, byFile.offset);
            if (el != null) {
                targets.add(el);
            }
        }

        // 2) 全量索引：所有 INSERT INTO 该表的文件（索引未命中才走这里，带时间预算）
        if (targets.isEmpty()) {
            List<TableNavigationService.InsertHit> hits = service.findInsertIntos(tableName);
            for (TableNavigationService.InsertHit h : hits) {
                PsiElement el = service.psiAt(h.file, h.offset);
                if (el != null) {
                    targets.add(el);
                }
            }
        }

        // 3) 兜底：CREATE TABLE 定义
        if (targets.isEmpty()) {
            PsiElement def = service.findCreateTableDefinition(tableName);
            if (def != null) {
                targets.add(def);
            }
        }

        if (targets.isEmpty()) {
            // 返回 null：交给平台默认逻辑（无定义的标识符会提示“找不到要转到”）
            return null;
        }
        return targets.toArray(new PsiElement[0]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        Editor editor = dataContext.getData(CommonDataKeys.EDITOR);
        if (editor != null) {
            VirtualFile vf = editor.getVirtualFile();
            if (vf != null) {
                String ext = vf.getExtension();
                if (ext != null && (ext.equalsIgnoreCase("sql") || ext.equalsIgnoreCase("hql"))) {
                    return "跳转到写入此表的文件";
                }
            }
        }
        return null;
    }
}
