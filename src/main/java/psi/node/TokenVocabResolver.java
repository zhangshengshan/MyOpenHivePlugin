package psi.node;

import antlr.g4.SqlBaseParser;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;
import plugin.HiveFile;

import static plugin.basic.HiveTokenTypes.RULE_ELEMENT_TYPES;

public class TokenVocabResolver {

    /**
     * If this reference is the value of a {@code tokenVocab} option, returns the corresponding
     * grammar file.
     */
    @Nullable
    public static PsiFile resolveTokenVocabFile(PsiElement reference) {
        PsiElement optionValue =
                PsiTreeUtil.findFirstParent(reference, TokenVocabResolver::isOptionValue);

        if (optionValue != null) {
            PsiElement option = optionValue.getParent();

            if (option != null) {
                PsiElement optionName = PsiTreeUtil.getDeepestFirst(option);

                if (optionName.getText().equals("tokenVocab")) {
//                    String text = StringUtils.strip(reference.getText(), "'");
                    String text = reference.getText().replaceAll("'", "");
                    return findRelativeFile(text, reference.getContainingFile());
                }
            }
        }

        return null;
    }

    /**
     * Tries to find a declaration named {@code ruleName} in the {@code tokenVocab} file if it exists.
     */


    private static boolean isOptionValue(PsiElement el) {
        ASTNode node = el.getNode();
        return node != null
                && node.getElementType() == RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_identifier);
    }

    /**
     * Looks for an ANTLR grammar file named {@code <baseName>}.g4 next to the given {@code sibling}
     * file.
     */
    static PsiFile findRelativeFile(String baseName, PsiFile sibling) {
        PsiDirectory parentDirectory = sibling.getParent();

        if (parentDirectory != null) {
            PsiFile candidate = parentDirectory.findFile(baseName + ".hql");

            if (candidate instanceof HiveFile) {
                return candidate;
            }
        }

        return null;
    }
}
