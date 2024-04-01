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
    return ctx.join().toString() + ctx.relation().toString()
  }

  override def visitRelation(ctx: MyDSLParser.RelationContext): String = {
    // TB (join TB)*
    if (ctx.TB().size() > 0) {
      val tbList = ctx.TB().asScala.map(_.getText).toList
      val joinList = ctx.join().asScala.map(visit).toList
      val combined = tbList
        .zipAll(joinList, "", "")
        .flatMap(pair => List(pair._1, pair._2))
        .mkString(" ")

      return combined
    }
    // relation join relation
    else if (ctx.relation().size() > 0) {
      val relations = ctx.relation().asScala.map(visit).toList
      val join = ctx.join().asScala.map(visit).toList // change this line
      val str = relations
        .zipAll(join, "", "")
        .flatMap(pair => List(pair._1, pair._2))
        .mkString(" ")
      return str

    }
    // LP relation RP
    else if (ctx.LP() != null && ctx.RP() != null) {
      val relation = visit(ctx.relation(0))
      return s"($relation)"
    } else {
      return ""
    }

  }

  override def visitJoin(ctx: MyDSLParser.JoinContext): String = {
    return "JOIN"
  }
}
