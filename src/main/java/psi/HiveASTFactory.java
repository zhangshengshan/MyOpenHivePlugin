package psi;

import antlr.g4.SqlBaseLexer;
import antlr.g4.SqlBaseParser;
import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import plugin.basic.HiveTokenTypes;
import psi.node.IdentifierNode;
import psi.node.LexerKeyWordNode;
import psi.node.MyRuleSpecNode;
import psi.node.StringLiteralElement;

import java.util.HashMap;
import java.util.Map;

import static plugin.basic.HiveTokenTypes.isKeyWord;

/**
 * @author zhangshengshan
 */
public class HiveASTFactory extends ASTFactory {
    private static final Map<IElementType, PsiElementFactory> ruleElementTypeToPsiFactory =
            new HashMap<>();

    static {
        ruleElementTypeToPsiFactory.put(
                HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_fromClause),
                MyRuleSpecNode.Factory.INSTANCE);
    }

    public static PsiElement createInternalParseTreeNode(ASTNode node) {
        PsiElement t;
        IElementType tokenType = node.getElementType();
        PsiElementFactory factory = ruleElementTypeToPsiFactory.get(tokenType);
        if (factory != null) {
            t = factory.createElement(node);
        } else {
            t = new ANTLRPsiNode(node);
        }
        return t;
    }

    /**
     * Create a FileElement for root or a parse tree CompositeElement (not PSI) for the token. This
     * impl is more or less the default.
     */
    @Override
    public CompositeElement createComposite(IElementType type) {
        //    if (type instanceof IFileElementType) {
        //      return new FileElement(type, null);
        //    }
        //    return new CompositeElement(type);
        return super.createComposite(type);
    }

    /**
     * Create PSI nodes out of tokens so even parse tree sees them as such. Does not see whitespace
     * tokens.
     */
    @Override
    public LeafElement createLeaf(IElementType type, CharSequence text) {
        LeafElement t;
        if (type == HiveTokenTypes.TOKEN_ELEMENT_TYPES.get(SqlBaseLexer.IDENTIFIER)) {
            t = new IdentifierNode(type, text);
        } else if (isKeyWord(type)) {
            t = new LexerKeyWordNode(type, text);
        } else if (type == HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_strictIdentifier)
                || type == HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_selectClause)
                || type == HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_query)) {
            // TODO: 2022/9/28 这个分支走不到
            t = new StringLiteralElement(type, text);
        } else {
            t = new StringLiteralElement(type, text);
        }
        return t;
    }
}
