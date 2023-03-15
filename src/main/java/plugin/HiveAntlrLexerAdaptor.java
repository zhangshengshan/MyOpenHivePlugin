package plugin;

import com.intellij.lang.Language;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.v4.runtime.Lexer;

public class HiveAntlrLexerAdaptor extends ANTLRLexerAdaptor {
    /**
     * Constructs a new instance of {@link ANTLRLexerAdaptor} with the specified {@link Language} and
     * underlying ANTLR {@link Lexer}.
     *
     * @param language The language.
     * @param lexer    The underlying ANTLR lexer.
     */
    public HiveAntlrLexerAdaptor(Language language, Lexer lexer) {
        super(language, lexer);
    }
}
