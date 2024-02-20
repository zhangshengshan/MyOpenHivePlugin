package action;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import misc.ClipBoardUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.application.ApplicationManager;

/**
 * @author nobody
 */
public class HiveFormatAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        final String format =
                SqlFormatter.of(Dialect.MySql)
                        .format(
                                editor.getDocument().getText(),
                                FormatConfig.builder()
                                        .indent("    ")
                                        .uppercase(true)
                                        .linesBetweenQueries(2)
                                        .maxColumnLength(100)
                                        .build());

        final Document document = editor.getDocument();
        ClipBoardUtil.copyToClipBoard(format);
        Messages.showInfoMessage("Formatted SQL has been copied to clipboard", "Success");

        int result = Messages.showYesNoDialog(
                project,
                "Do you want to replace the original SQL with the formatted SQL?",
                "Confirmation",
                Messages.getQuestionIcon()
        );

        // Replace the original SQL with the formatted SQL
        if (result == Messages.YES) {
            try{
                ApplicationManager.getApplication().runWriteAction(() -> document.setText(format));
            } catch (Exception e) {
                Messages.showInfoMessage("Failed to replace the original SQL with the formatted SQL", "Error");
            }
        }
    }
}
