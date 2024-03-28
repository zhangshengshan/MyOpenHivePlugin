// Generated from E:/¹¤×÷´æµµ/MyOpenHivePlugin/src/main/scala/antlr4/dsl/MyDSLLexer.g4 by ANTLR 4.13.1
package antlr4.dsl;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class MyDSLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TB=1, JOIN=2, LEFT_JOIN=3, RIGHT_JOIN=4, FULL_JOIN=5, SEMICOLON=6, LP=7, 
		RP=8, COMMA=9, DOT=10, LEFT_BRACKET=11, RIGHT_BRACKET=12, LEFT_BRACE=13, 
		RIGHT_BRACE=14, SPACE=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TB", "JOIN", "LEFT_JOIN", "RIGHT_JOIN", "FULL_JOIN", "SEMICOLON", "LP", 
			"RP", "COMMA", "DOT", "LEFT_BRACKET", "RIGHT_BRACKET", "LEFT_BRACE", 
			"RIGHT_BRACE", "SPACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'-'", "'->'", "'<-'", "'<->'", "';'", "'('", "')'", "','", 
			"'.'", "'['", "']'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TB", "JOIN", "LEFT_JOIN", "RIGHT_JOIN", "FULL_JOIN", "SEMICOLON", 
			"LP", "RP", "COMMA", "DOT", "LEFT_BRACKET", "RIGHT_BRACKET", "LEFT_BRACE", 
			"RIGHT_BRACE", "SPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MyDSLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MyDSLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u000fI\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001"+
		"\u0000\u0004\u0000!\b\u0000\u000b\u0000\f\u0000\"\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0004\u000eD\b\u000e\u000b\u000e\f\u000eE\u0001"+
		"\u000e\u0001\u000e\u0000\u0000\u000f\u0001\u0001\u0003\u0002\u0005\u0003"+
		"\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015"+
		"\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u0001\u0000\u0002\u0004"+
		"\u000009AZ__az\u0003\u0000\t\n\r\r  J\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0001 \u0001"+
		"\u0000\u0000\u0000\u0003$\u0001\u0000\u0000\u0000\u0005&\u0001\u0000\u0000"+
		"\u0000\u0007)\u0001\u0000\u0000\u0000\t,\u0001\u0000\u0000\u0000\u000b"+
		"0\u0001\u0000\u0000\u0000\r2\u0001\u0000\u0000\u0000\u000f4\u0001\u0000"+
		"\u0000\u0000\u00116\u0001\u0000\u0000\u0000\u00138\u0001\u0000\u0000\u0000"+
		"\u0015:\u0001\u0000\u0000\u0000\u0017<\u0001\u0000\u0000\u0000\u0019>"+
		"\u0001\u0000\u0000\u0000\u001b@\u0001\u0000\u0000\u0000\u001dC\u0001\u0000"+
		"\u0000\u0000\u001f!\u0007\u0000\u0000\u0000 \u001f\u0001\u0000\u0000\u0000"+
		"!\"\u0001\u0000\u0000\u0000\" \u0001\u0000\u0000\u0000\"#\u0001\u0000"+
		"\u0000\u0000#\u0002\u0001\u0000\u0000\u0000$%\u0005-\u0000\u0000%\u0004"+
		"\u0001\u0000\u0000\u0000&\'\u0005-\u0000\u0000\'(\u0005>\u0000\u0000("+
		"\u0006\u0001\u0000\u0000\u0000)*\u0005<\u0000\u0000*+\u0005-\u0000\u0000"+
		"+\b\u0001\u0000\u0000\u0000,-\u0005<\u0000\u0000-.\u0005-\u0000\u0000"+
		"./\u0005>\u0000\u0000/\n\u0001\u0000\u0000\u000001\u0005;\u0000\u0000"+
		"1\f\u0001\u0000\u0000\u000023\u0005(\u0000\u00003\u000e\u0001\u0000\u0000"+
		"\u000045\u0005)\u0000\u00005\u0010\u0001\u0000\u0000\u000067\u0005,\u0000"+
		"\u00007\u0012\u0001\u0000\u0000\u000089\u0005.\u0000\u00009\u0014\u0001"+
		"\u0000\u0000\u0000:;\u0005[\u0000\u0000;\u0016\u0001\u0000\u0000\u0000"+
		"<=\u0005]\u0000\u0000=\u0018\u0001\u0000\u0000\u0000>?\u0005{\u0000\u0000"+
		"?\u001a\u0001\u0000\u0000\u0000@A\u0005}\u0000\u0000A\u001c\u0001\u0000"+
		"\u0000\u0000BD\u0007\u0001\u0000\u0000CB\u0001\u0000\u0000\u0000DE\u0001"+
		"\u0000\u0000\u0000EC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000"+
		"FG\u0001\u0000\u0000\u0000GH\u0006\u000e\u0000\u0000H\u001e\u0001\u0000"+
		"\u0000\u0000\u0003\u0000\"E\u0001\u0000\u0001\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}