package psi.node;

import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * Refs to tokens, rules
 */
public abstract class BaseGrammarElementRefNode extends LeafPsiElement implements PsiNamedElement {
    protected String name = null;

    public BaseGrammarElementRefNode(IElementType type, CharSequence text) {
        super(type, text);
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }
        return getText();
    }

/*
  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    throw new IncorrectOperationException("Can't rename grammar reference identifier");
  }
*/

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getElementType() + ")";
    }
}
