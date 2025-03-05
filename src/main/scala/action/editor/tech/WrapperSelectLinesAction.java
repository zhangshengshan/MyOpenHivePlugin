package action.editor.tech;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.NotNull;

public class WrapperSelectLinesAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Get the editor and selection model
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        SelectionModel selectionModel = editor.getSelectionModel();
        Document document = editor.getDocument();

        // Get the selected text
        int start = selectionModel.getSelectionStart();
        int end = selectionModel.getSelectionEnd();
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            return;
        }

        // Add parentheses around the selected text
        String newText = "(\r" + selectedText + "\r) AS ";

        // Replace the selected text with the new text
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            document.replaceString(start, end, newText);
            // Move the cursor to the position after "AS "
            editor.getCaretModel().moveToOffset(start + newText.length());
        });
    }
}