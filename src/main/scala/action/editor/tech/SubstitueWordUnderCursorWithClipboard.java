package action.editor.tech;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;

public class SubstitueWordUnderCursorWithClipboard extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // 获取项目和编辑器实例
        Project project = e.getData(CommonDataKeys.PROJECT);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (project == null || editor == null) {
            return;
        }

        // 获取当前光标位置
        CaretModel caretModel = editor.getCaretModel();
        int caretOffset = caretModel.getOffset();

        // 获取文档
        Document document = editor.getDocument();
        CharSequence documentText = document.getCharsSequence();

        // 获取当前光标下的词
        final int start = getWordStart(documentText, caretOffset);
        final int end = getWordEnd(documentText, caretOffset);

        // 获取剪切板中的内容
        String clipboardText;
        try {
            clipboardText = (String) CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return;
        }

        if (clipboardText == null || clipboardText.isEmpty()) {
            return;
        }

        // 用剪切板中的内容替换当前光标下的词
        WriteCommandAction.runWriteCommandAction(project, () ->
            document.replaceString(start, end, clipboardText)
        );
    }

    private int getWordStart(CharSequence text, int offset) {
        int start = offset;
        while (start > 0 && Character.isJavaIdentifierPart(text.charAt(start - 1))) {
            start--;
        }
        return start;
    }

    private int getWordEnd(CharSequence text, int offset) {
        int end = offset;
        while (end < text.length() && Character.isJavaIdentifierPart(text.charAt(end))) {
            end++;
        }
        return end;
    }
}