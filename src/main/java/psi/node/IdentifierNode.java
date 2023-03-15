package psi.node;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * @author nobody
 */
public class IdentifierNode extends GrammarElementRefNode {
    public IdentifierNode(IElementType type, CharSequence text) {
        super(type, text);
    }

/*  @Override
  public PsiReference @NotNull [] getReferences() {

    final PsiElement root = PsiTreeUtil.getTopmostParentOfType(this, PsiElement.class);

    final PsiElement[] psiElements =
        PsiTreeUtil.collectElements(
            root,
            element -> {
              if (element instanceof IdentifierNode && element.getText() == this.getText()) {
                if (((IdentifierNode) element).getElementType()
                    == HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_identifier)) {
                  return true;
                }
              }
              return false;
            });

    PsiReference[] references = new PsiReference[psiElements.length];
    for (int i = 0; i < psiElements.length; i++) {
      references[i] = psiElements[i].getReference();
    }
    return references;
  }*/

    @Override
    public PsiReference getReference() {
        return new GrammarElementRef(this, getText());
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        this.name = name;
        return this;
    }
}
