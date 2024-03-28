// Generated from E:/¹¤×÷´æµµ/MyOpenHivePlugin/src/main/scala/antlr4/dsl/MyDSLParser.g4 by ANTLR 4.13.1
package antlr4.dsl;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MyDSLParser}.
 */
public interface MyDSLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MyDSLParser#mutilroots}.
	 * @param ctx the parse tree
	 */
	void enterMutilroots(MyDSLParser.MutilrootsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyDSLParser#mutilroots}.
	 * @param ctx the parse tree
	 */
	void exitMutilroots(MyDSLParser.MutilrootsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyDSLParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(MyDSLParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyDSLParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(MyDSLParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(MyDSLParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(MyDSLParser.RelationContext ctx);
}