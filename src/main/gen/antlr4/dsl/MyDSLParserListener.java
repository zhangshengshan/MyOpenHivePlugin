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
	 * Enter a parse tree produced by {@link MyDSLParser#join_rel}.
	 * @param ctx the parse tree
	 */
	void enterJoin_rel(MyDSLParser.Join_relContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyDSLParser#join_rel}.
	 * @param ctx the parse tree
	 */
	void exitJoin_rel(MyDSLParser.Join_relContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationJoinRelation}
	 * labeled alternative in {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelationJoinRelation(MyDSLParser.RelationJoinRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationJoinRelation}
	 * labeled alternative in {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelationJoinRelation(MyDSLParser.RelationJoinRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenRelation}
	 * labeled alternative in {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterParenRelation(MyDSLParser.ParenRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenRelation}
	 * labeled alternative in {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitParenRelation(MyDSLParser.ParenRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TbJoinTb}
	 * labeled alternative in {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterTbJoinTb(MyDSLParser.TbJoinTbContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TbJoinTb}
	 * labeled alternative in {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitTbJoinTb(MyDSLParser.TbJoinTbContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyDSLParser#join}.
	 * @param ctx the parse tree
	 */
	void enterJoin(MyDSLParser.JoinContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyDSLParser#join}.
	 * @param ctx the parse tree
	 */
	void exitJoin(MyDSLParser.JoinContext ctx);
}