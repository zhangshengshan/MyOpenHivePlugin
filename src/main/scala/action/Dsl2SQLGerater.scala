package action

import antlr4.dsl.{MyDSLParser, MyDSLParserBaseVisitor}

import java.util
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

class Dsl2SQLGerater extends MyDSLParserBaseVisitor[String]{

  var ret = ""

  override def visitRoot(ctx: MyDSLParser.RootContext): String = {
    val contexts: util.List[MyDSLParser.Join_relContext] = ctx.join_rel()
    contexts.toList.foreach(x => x.toString())

    ret = ret + ctx.join_rel().toString()
    return ctx.relation().toString()
  }

  override def visitJoin_rel(ctx: MyDSLParser.Join_relContext): String = {
    ctx.join().toString() + ctx.relation().toString()
  }

  override def visitRelation(ctx: MyDSLParser.RelationContext): String = {
    null
  }
}
