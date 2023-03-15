package plugin;

import antlr.g4.SqlBaseLexer;
import antlr.g4.SqlBaseParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import psi.HiveASTFactory;

import java.util.List;

import static antlr.g4.SqlBaseLexer.*;

/**
 * @author nobody
 */
public class HiveParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS_TMP;
    public static final TokenSet WHITESPACE;
    public static final TokenSet STRING;
    public static TokenIElementType ID;

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                HiveLanguage.INSTANCE, tokenNames, SqlBaseParser.ruleNames);
        List<TokenIElementType> tokenIElementTypes =
                PSIElementTypeFactory.getTokenIElementTypes(HiveLanguage.INSTANCE);
        ID = tokenIElementTypes.get(IDENTIFIER);
        COMMENTS_TMP =
                PSIElementTypeFactory.createTokenSet(
                        HiveLanguage.INSTANCE, COMMENT, SIMPLE_COMMENT, BRACKETED_COMMENT);
        WHITESPACE = PSIElementTypeFactory.createTokenSet(HiveLanguage.INSTANCE, WS);
        STRING = PSIElementTypeFactory.createTokenSet(HiveLanguage.INSTANCE, SqlBaseLexer.STRING);
    }

    public final IFileElementType FILE = new IFileElementType(HiveLanguage.INSTANCE);

    public HiveParserDefinition() {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                HiveLanguage.INSTANCE, tokenNames, SqlBaseParser.ruleNames);
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        SqlBaseLexer lexer = new SqlBaseLexer(null);
        return new HiveAntlrLexerAdaptor(HiveLanguage.INSTANCE, lexer);
    }

    @Override
    public PsiParser createParser(Project project) {
        return new HiveAntlrParserAdaptor();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS_TMP;
    }

    @Override
    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRING;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return HiveASTFactory.createInternalParseTreeNode(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new HiveFile(viewProvider);
    }
}
