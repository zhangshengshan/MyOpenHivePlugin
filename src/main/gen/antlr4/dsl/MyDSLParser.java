// Generated from E:/¹¤×÷´æµµ/MyOpenHivePlugin/src/main/scala/antlr4/dsl/MyDSLParser.g4 by ANTLR 4.13.1
package antlr4.dsl;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MyDSLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TB=1, JOIN=2, LEFT_JOIN=3, RIGHT_JOIN=4, FULL_JOIN=5, SEMICOLON=6, LP=7, 
		RP=8, COMMA=9, DOT=10, LEFT_BRACKET=11, RIGHT_BRACKET=12, LEFT_BRACE=13, 
		RIGHT_BRACE=14, SPACE=15;
	public static final int
		RULE_mutilroots = 0, RULE_root = 1, RULE_relation = 2, RULE_join = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"mutilroots", "root", "relation", "join"
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

	@Override
	public String getGrammarFileName() { return "MyDSLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MyDSLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MutilrootsContext extends ParserRuleContext {
		public List<RootContext> root() {
			return getRuleContexts(RootContext.class);
		}
		public RootContext root(int i) {
			return getRuleContext(RootContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(MyDSLParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(MyDSLParser.SEMICOLON, i);
		}
		public MutilrootsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mutilroots; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).enterMutilroots(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).exitMutilroots(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MyDSLParserVisitor ) return ((MyDSLParserVisitor<? extends T>)visitor).visitMutilroots(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MutilrootsContext mutilroots() throws RecognitionException {
		MutilrootsContext _localctx = new MutilrootsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_mutilroots);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			root();
			setState(13);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(9);
					match(SEMICOLON);
					setState(10);
					root();
					}
					} 
				}
				setState(15);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMICOLON) {
				{
				setState(16);
				match(SEMICOLON);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RootContext extends ParserRuleContext {
		public List<RelationContext> relation() {
			return getRuleContexts(RelationContext.class);
		}
		public RelationContext relation(int i) {
			return getRuleContext(RelationContext.class,i);
		}
		public List<JoinContext> join() {
			return getRuleContexts(JoinContext.class);
		}
		public JoinContext join(int i) {
			return getRuleContext(JoinContext.class,i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MyDSLParserVisitor ) return ((MyDSLParserVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			relation(0);
			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 60L) != 0)) {
				{
				{
				setState(20);
				join();
				setState(21);
				relation(0);
				}
				}
				setState(27);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationContext extends ParserRuleContext {
		public List<TerminalNode> TB() { return getTokens(MyDSLParser.TB); }
		public TerminalNode TB(int i) {
			return getToken(MyDSLParser.TB, i);
		}
		public List<JoinContext> join() {
			return getRuleContexts(JoinContext.class);
		}
		public JoinContext join(int i) {
			return getRuleContext(JoinContext.class,i);
		}
		public TerminalNode LP() { return getToken(MyDSLParser.LP, 0); }
		public List<RelationContext> relation() {
			return getRuleContexts(RelationContext.class);
		}
		public RelationContext relation(int i) {
			return getRuleContext(RelationContext.class,i);
		}
		public TerminalNode RP() { return getToken(MyDSLParser.RP, 0); }
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).exitRelation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MyDSLParserVisitor ) return ((MyDSLParserVisitor<? extends T>)visitor).visitRelation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		return relation(0);
	}

	private RelationContext relation(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		RelationContext _localctx = new RelationContext(_ctx, _parentState);
		RelationContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_relation, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TB:
				{
				setState(29);
				match(TB);
				setState(35);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(30);
						join();
						setState(31);
						match(TB);
						}
						} 
					}
					setState(37);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				}
				}
				break;
			case LP:
				{
				setState(38);
				match(LP);
				setState(39);
				relation(0);
				setState(40);
				match(RP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(50);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new RelationContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_relation);
					setState(44);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(45);
					join();
					setState(46);
					relation(3);
					}
					} 
				}
				setState(52);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JoinContext extends ParserRuleContext {
		public TerminalNode JOIN() { return getToken(MyDSLParser.JOIN, 0); }
		public TerminalNode LEFT_JOIN() { return getToken(MyDSLParser.LEFT_JOIN, 0); }
		public TerminalNode RIGHT_JOIN() { return getToken(MyDSLParser.RIGHT_JOIN, 0); }
		public TerminalNode FULL_JOIN() { return getToken(MyDSLParser.FULL_JOIN, 0); }
		public JoinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_join; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).enterJoin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyDSLParserListener ) ((MyDSLParserListener)listener).exitJoin(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MyDSLParserVisitor ) return ((MyDSLParserVisitor<? extends T>)visitor).visitJoin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinContext join() throws RecognitionException {
		JoinContext _localctx = new JoinContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_join);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 60L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return relation_sempred((RelationContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean relation_sempred(RelationContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u000f8\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0005\u0000\f\b\u0000\n\u0000\f\u0000\u000f\t\u0000\u0001\u0000"+
		"\u0003\u0000\u0012\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0005\u0001\u0018\b\u0001\n\u0001\f\u0001\u001b\t\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\"\b\u0002\n\u0002"+
		"\f\u0002%\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002+\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005"+
		"\u00021\b\u0002\n\u0002\f\u00024\t\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0000\u0001\u0004\u0004\u0000\u0002\u0004\u0006\u0000\u0001\u0001"+
		"\u0000\u0002\u00059\u0000\b\u0001\u0000\u0000\u0000\u0002\u0013\u0001"+
		"\u0000\u0000\u0000\u0004*\u0001\u0000\u0000\u0000\u00065\u0001\u0000\u0000"+
		"\u0000\b\r\u0003\u0002\u0001\u0000\t\n\u0005\u0006\u0000\u0000\n\f\u0003"+
		"\u0002\u0001\u0000\u000b\t\u0001\u0000\u0000\u0000\f\u000f\u0001\u0000"+
		"\u0000\u0000\r\u000b\u0001\u0000\u0000\u0000\r\u000e\u0001\u0000\u0000"+
		"\u0000\u000e\u0011\u0001\u0000\u0000\u0000\u000f\r\u0001\u0000\u0000\u0000"+
		"\u0010\u0012\u0005\u0006\u0000\u0000\u0011\u0010\u0001\u0000\u0000\u0000"+
		"\u0011\u0012\u0001\u0000\u0000\u0000\u0012\u0001\u0001\u0000\u0000\u0000"+
		"\u0013\u0019\u0003\u0004\u0002\u0000\u0014\u0015\u0003\u0006\u0003\u0000"+
		"\u0015\u0016\u0003\u0004\u0002\u0000\u0016\u0018\u0001\u0000\u0000\u0000"+
		"\u0017\u0014\u0001\u0000\u0000\u0000\u0018\u001b\u0001\u0000\u0000\u0000"+
		"\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000"+
		"\u001a\u0003\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000\u0000\u0000"+
		"\u001c\u001d\u0006\u0002\uffff\uffff\u0000\u001d#\u0005\u0001\u0000\u0000"+
		"\u001e\u001f\u0003\u0006\u0003\u0000\u001f \u0005\u0001\u0000\u0000 \""+
		"\u0001\u0000\u0000\u0000!\u001e\u0001\u0000\u0000\u0000\"%\u0001\u0000"+
		"\u0000\u0000#!\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$+\u0001"+
		"\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000&\'\u0005\u0007\u0000\u0000"+
		"\'(\u0003\u0004\u0002\u0000()\u0005\b\u0000\u0000)+\u0001\u0000\u0000"+
		"\u0000*\u001c\u0001\u0000\u0000\u0000*&\u0001\u0000\u0000\u0000+2\u0001"+
		"\u0000\u0000\u0000,-\n\u0002\u0000\u0000-.\u0003\u0006\u0003\u0000./\u0003"+
		"\u0004\u0002\u0003/1\u0001\u0000\u0000\u00000,\u0001\u0000\u0000\u0000"+
		"14\u0001\u0000\u0000\u000020\u0001\u0000\u0000\u000023\u0001\u0000\u0000"+
		"\u00003\u0005\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u000056\u0007"+
		"\u0000\u0000\u00006\u0007\u0001\u0000\u0000\u0000\u0006\r\u0011\u0019"+
		"#*2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}