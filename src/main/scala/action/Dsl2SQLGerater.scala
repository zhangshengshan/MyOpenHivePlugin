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
    val tb = ctx.TB(0).getText // 访问第一个 TB
    val result = new StringBuilder(tb)
    // 遍历所有的 join TB
    for (i <- 1 until ctx.TB().size()) {
      val join = visit(ctx.join(i - 1))
      val tb = ctx.TB(i).getText // 访问 TB
      result.append(s" $join $tb") // 添加到结果字符串中
    }
    result.toString()
  }

  override def visitRelationJoinRelation(
      ctx: MyDSLParser.RelationJoinRelationContext
  ): String = {
    val relation1 = visit(ctx.relation(0)) // 访问第一个 relation
    val join = visit(ctx.join()) // 访问 join
    val relation2 = visit(ctx.relation(1)) // 访问第二个 relation
    s"$relation1 $join $relation2" // 返回结果字符串
  }

  override def visitParenRelation(
      ctx: MyDSLParser.ParenRelationContext
  ): String = {
    val relationStr = visit(ctx.relation())
    return "(" + relationStr + ")"
  }

//  join: JOIN | LEFT_JOIN | RIGHT_JOIN | FULL_JOIN  ;
  override def visitJoin(ctx: MyDSLParser.JoinContext): String = {
    // 判断 join 类型，返回对应的字符串
    val joinStr = Option(ctx.LEFT_JOIN())
      .map(_ => "LEFT JOIN")
      .getOrElse(
        Option(ctx.RIGHT_JOIN())
          .map(_ => "RIGHT JOIN")
          .getOrElse(
            Option(ctx.FULL_JOIN())
              .map(_ => "FULL JOIN")
              .getOrElse(
                Option(ctx.JOIN())
                  .map(_ => "JOIN")
                  .getOrElse("")
              )
          )
      )
    joinStr
  }
}
