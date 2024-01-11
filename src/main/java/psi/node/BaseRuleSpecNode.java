package psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import psi.MyPsiUtils;

/**
 * Root of lexer, parser rule defs
 *
 * @author alphabetago
 */
public abstract class BaseRuleSpecNode extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {
    protected String name = null;

    public BaseRuleSpecNode(@NotNull final ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }
        @Nullable ANTLRPsiNode id = getNameIdentifier();
        if (id != null) {
            return id.getText();
        }
        return "unknown-name";
    }

    @Override
    public abstract @Nullable ANTLRPsiNode getNameIdentifier();

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    /*
    From doc: "Creating a fully correct AST node from scratch is
              quite difficult. Thus, surprisingly, the easiest way to
              get the replacement node is to create a dummy file in the
              custom language so that it would contain the necessary
              node in its parse tree, build the parse tree and
              extract the necessary node from it.
     */
        @Nullable ANTLRPsiNode id = getNameIdentifier();
        id.replace(MyPsiUtils.createLeafFromText(getProject(), getContext(), name, getRuleRefType()));
        this.name = name;
        return this;
    }

    public abstract IElementType getRuleRefType();

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        name = null;
    }

    @Override
    public int getTextOffset() {
        @Nullable ANTLRPsiNode id = getNameIdentifier();
        if (id != null) {
            return id.getTextOffset();
        }
        return super.getTextOffset();
    }
}
