package psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * This interface supports constructing a {@link PsiElement} from an {@link ASTNode}.
 * @author zhangshengshan
 */
public interface PsiElementFactory {
    /**
     * @param node
     * @return
     */
    PsiElement createElement(ASTNode node);
}
