package navigation;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则版表跳转：不依赖 PSI 节点类型，仅从光标处用正则提取表名，
 * 再扫描项目所有 .hql/.sql，找到“包含表名且含 insert into 表名”的文件，打开并定位到 INSERT 行。
 * 多个命中时弹出选择列表。
 */
public class GotoWriteTableFileAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        boolean enabled = project != null && editor != null && psiFile != null
                && TableNavigationService.extractTableName(editor) != null;
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (project == null || editor == null || psiFile == null) {
            return;
        }

        String tableName = TableNavigationService.extractTableName(editor);
        if (tableName == null || tableName.isEmpty()) {
            return;
        }

        TableNavigationService service = TableNavigationService.getInstance(project);

        // 快路径：文件名精确等于表名的文件（<表名>.sql/.hql），只读这一个文件
        TableNavigationService.InsertHit byFile = service.findSameNamedFile(tableName);
        if (byFile != null) {
            openHit(project, byFile);
            return;
        }

        List<TableNavigationService.InsertHit> hits = service.findInsertIntos(tableName);

        if (hits.isEmpty()) {
            Messages.showInfoMessage("没有找到写入表 " + tableName + " 的文件", "跳转结果");
            return;
        }

        if (hits.size() == 1) {
            openHit(project, hits.get(0));
            return;
        }

        List<TableNavigationService.InsertHit> finalHits = new ArrayList<>(hits);
        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(finalHits)
                .setTitle("选择写入 " + tableName + " 的文件")
                .setItemChosenCallback(h -> openHit(project, h))
                .createPopup()
                .showInBestPositionFor(e.getDataContext());
    }

    private static void openHit(@NotNull Project project, @NotNull TableNavigationService.InsertHit h) {
        // 定位到该文件内 INSERT 语句中表名 token 的偏移处
        new OpenFileDescriptor(project, h.file, h.offset).navigate(true);
    }
}
