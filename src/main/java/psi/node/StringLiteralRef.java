package psi.node;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.ArrayUtilRt;
import org.jetbrains.annotations.Nullable;

/**
 * @author shengshan.zhang
 */
public class StringLiteralRef extends PsiReferenceBase<StringLiteralElement> {

    public StringLiteralRef(StringLiteralElement node) {
        super(node, TextRange.from(1, node.getTextLength() - 2));
    }

    @Override
    public Object[] getVariants() {
        return ArrayUtilRt.EMPTY_OBJECT_ARRAY;
    }

    @Override
    public @Nullable PsiElement resolve() {
        return TokenVocabResolver.resolveTokenVocabFile(myElement);
    }
}
