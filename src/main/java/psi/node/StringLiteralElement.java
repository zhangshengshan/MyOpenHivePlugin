package psi.node;

import antlr.g4.SqlBaseParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;

import static plugin.basic.HiveTokenTypes.RULE_ELEMENT_TYPES;

/**
 * @author nobody
 */
public class StringLiteralElement extends LeafPsiElement {
    public StringLiteralElement(IElementType type, CharSequence text) {
        super(type, text);
    }

    @Override
    public PsiReference getReference() {
        PsiElement parent = getParent();

        if (parent != null
                && parent.getNode().getElementType()
                == RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_identifier)) {
            return new StringLiteralRef(this);
        }

        return super.getReference();
    }
}
