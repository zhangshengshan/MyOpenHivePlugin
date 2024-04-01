// Generated from E:/¹¤×÷´æµµ/MyOpenHivePlugin/src/main/scala/antlr4/dsl/MyDSLParser.g4 by ANTLR 4.13.1
package antlr4.dsl;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MyDSLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MyDSLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MyDSLParser#mutilroots}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMutilroots(MyDSLParser.MutilrootsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MyDSLParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(MyDSLParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link MyDSLParser#join_rel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_rel(MyDSLParser.Join_relContext ctx);
	/**
	 * Visit a parse tree produced by {@link MyDSLParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(MyDSLParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MyDSLParser#join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin(MyDSLParser.JoinContext ctx);
}