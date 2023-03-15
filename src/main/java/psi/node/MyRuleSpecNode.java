package psi.node;

import antlr.g4.SqlBaseParser;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.basic.HiveTokenTypes;
import psi.PsiElementFactory;

/**
 * @author nobody
 */
public class MyRuleSpecNode extends RuleSpecNode {
    public static final Logger LOG = Logger.getInstance(MyRuleSpecNode.class);

    public MyRuleSpecNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public IElementType getRuleRefType() {
        return HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_fromClause);
    }

    @Override
    public @org.jetbrains.annotations.Nullable ANTLRPsiNode getNameIdentifier() {
        @Nullable ANTLRPsiNode rr = PsiTreeUtil.getChildOfType(this, ANTLRPsiNode.class);
        if (rr == null) {
            LOG.error("text = " + getText());
            LOG.error("can't find ParserRuleRefNode child of " + this.getText(), (Throwable) null);
        } else {
            LOG.debug(rr.getText());
            LOG.debug("get first child!");
        }
        return rr;
    }

    public static class Factory implements PsiElementFactory {
        public static Factory INSTANCE = new Factory();

        @Override
        public PsiElement createElement(ASTNode node) {
            return new MyRuleSpecNode(node);
        }
    }
}
