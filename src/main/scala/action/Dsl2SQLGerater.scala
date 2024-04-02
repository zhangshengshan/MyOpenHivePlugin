package action

import antlr4.dsl.{MyDSLParser, MyDSLParserBaseVisitor}

import java.util
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

class Dsl2SQLGerater extends MyDSLParserBaseVisitor[String] {

  var ret = ""

  override def visitRoot(ctx: MyDSLParser.RootContext): String = {
    val primary_relation: String = visit(ctx.relation())
    ret += primary_relation
    val contexts: util.List[MyDSLParser.Join_relContext] = ctx.join_rel()
    contexts.foreach(x => {
      val str = visit(x)
      str
      ret + str
    })

    null
  }

  override def visitJoin_rel(ctx: MyDSLParser.Join_relContext): String = {

    val joinStr = ctx.join().toString()

    val relationStr = visit(ctx.relation())

    return joinStr + " " + relationStr
  }

  override def visitTbJoinTb(ctx: MyDSLParser.TbJoinTbContext): String = {



    null
  }

  override def visitRelationJoinRelation(
      ctx: MyDSLParser.RelationJoinRelationContext
  ): String = {
    null
  }

  override def visitParenRelation(
      ctx: MyDSLParser.ParenRelationContext
  ): String = {
    val relationStr = visit(ctx.relation())
    return "(" + relationStr + ")"
  }

  override def visitJoin(ctx: MyDSLParser.JoinContext): String = {
    return "JOIN"
  }
}
