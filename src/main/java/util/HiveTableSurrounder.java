package util;

import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HiveTableSurrounder implements Surrounder {
    @Override
    public @NlsActions.ActionText String getTemplateDescription() {
        return "hive.surroud";
    }

    @Override
    public boolean isApplicable(PsiElement @NotNull [] elements) {
        return true;
    }

    @Override
    public @Nullable TextRange surroundElements(
            @NotNull Project project, @NotNull Editor editor, PsiElement @NotNull [] elements)
            throws IncorrectOperationException {

        final int length = elements.length;

        // TODO: 2022/10/14 这里似乎未能按照预期执行
        if (length != 1) {
            Messages.showErrorDialog("Please select only one element", "Error");
            throw new IncorrectOperationException("Only one element can be surrounded for now");
        }

        final PsiFileFactory factory = PsiFileFactory.getInstance(project);
        final String s = System.lineSeparator();
        final PsiFile f =
                factory.createFileFromText(
                        "dummy.regexp",
                        RegExpFileType.INSTANCE,
                        "(" + s + "SELECT * FROM " + elements[0].getText() + s + ")");
        elements[0].replace(f.getFirstChild());

        return null;
    }
}
