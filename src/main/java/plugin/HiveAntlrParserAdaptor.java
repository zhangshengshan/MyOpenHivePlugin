package plugin;

import antlr.g4.SqlBaseParser;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author nobody
 */
public class HiveAntlrParserAdaptor extends ANTLRParserAdaptor {

    public HiveAntlrParserAdaptor() {
        super(HiveLanguage.INSTANCE, new SqlBaseParser(null));
    }

    @Override
    protected ParseTree parse(Parser parser, IElementType root) {
        return ((SqlBaseParser) parser).singleStatement();
    }
}
