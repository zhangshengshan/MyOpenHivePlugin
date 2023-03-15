package hilight;

import antlr.g4.SqlBaseLexer;
import antlr.g4.SqlBaseParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import plugin.HiveLanguage;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * @author nobody
 */
public class HiveSyntaxHighlighter extends SyntaxHighlighterBase {

    /**
     * TODO: Syntax Highlight definition remove out from this class
     */
    public static final TextAttributesKey ID =
            createTextAttributesKey("ID", DefaultLanguageHighlighterColors.IDENTIFIER);

    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey LITERAL =
            createTextAttributesKey("LITERAL", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey OPERATORS =
            createTextAttributesKey("OPERATORS", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey LINE_COMMENT =
            createTextAttributesKey("LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            createTextAttributesKey("BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    /** TODO: Could the static block be deleted? */
    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                HiveLanguage.INSTANCE, SqlBaseLexer.tokenNames, SqlBaseParser.ruleNames);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        SqlBaseLexer lexer = new SqlBaseLexer(null);
        return new ANTLRLexerAdaptor(HiveLanguage.INSTANCE, lexer);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        /** TODO:highlight strategy should be refactored */
        if (!(tokenType instanceof TokenIElementType)) {
            return EMPTY_KEYS;
        }
        TokenIElementType myType = (TokenIElementType) tokenType;
        int ttype = myType.getANTLRTokenType();
        TextAttributesKey attrKey;

        if (ttype <= 311 || ttype == SqlBaseLexer.SELECT) {
            attrKey = KEYWORD;
        } else if (ttype == SqlBaseLexer.IDENTIFIER) {
            attrKey = LITERAL;
        } else {
            return EMPTY_KEYS;
        }
        return new TextAttributesKey[]{attrKey};
    }
}
