package plugin.basic;

import antlr.g4.SqlBaseLexer;
import antlr.g4.SqlBaseParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.intellij.lang.annotations.MagicConstant;
import plugin.HiveLanguage;

import java.util.List;

public class HiveTokenTypes {
    public static final List<TokenIElementType> TOKEN_ELEMENT_TYPES =
            PSIElementTypeFactory.getTokenIElementTypes(HiveLanguage.INSTANCE);
    public static final List<RuleIElementType> RULE_ELEMENT_TYPES =
            PSIElementTypeFactory.getRuleIElementTypes(HiveLanguage.INSTANCE);
    public static final TokenSet COMMENTS =
            PSIElementTypeFactory.createTokenSet(
                    HiveLanguage.INSTANCE, SqlBaseLexer.COMMENT, SqlBaseLexer.SIMPLE_COMMENT);
    public static final TokenSet WHITESPACES =
            PSIElementTypeFactory.createTokenSet(HiveLanguage.INSTANCE, SqlBaseLexer.WS);
    public static final TokenSet KEYWORDS =
            PSIElementTypeFactory.createTokenSet(
                    HiveLanguage.INSTANCE,
                    SqlBaseLexer.FROM,
                    SqlBaseLexer.SELECT,
                    SqlBaseLexer.FROM,
                    SqlBaseLexer.WHERE);

    public static final TokenSet ID =
            PSIElementTypeFactory.createTokenSet(HiveLanguage.INSTANCE, SqlBaseLexer.IDENTIFIER);

    public static IElementType BAD_TOKEN_TYPE = new IElementType("BAD_TOKEN", HiveLanguage.INSTANCE);

    /*
        DEBUG
    */
    static {
        for (int i = 0; i < TOKEN_ELEMENT_TYPES.size(); i++) {
            //      System.out.println(TOKEN_ELEMENT_TYPES.get(i).toString());
        }
    }

    public static RuleIElementType getRuleElementType(
            @MagicConstant(valuesFromClass = SqlBaseParser.class) int ruleIndex) {
        return RULE_ELEMENT_TYPES.get(ruleIndex);
    }

    public static TokenIElementType getTokenElementType(
            @MagicConstant(valuesFromClass = SqlBaseLexer.class) int ruleIndex) {
        return TOKEN_ELEMENT_TYPES.get(ruleIndex);
    }

    // TODO: 2022/9/28 需要剔除诸如IDENTIFIER  STRING 这样的TOKEN， 仅保留SELECT  WHERE  FROM 之类的关键字
    public static Boolean isKeyWord(IElementType type) {
        return TOKEN_ELEMENT_TYPES.contains(type);
    }
}
