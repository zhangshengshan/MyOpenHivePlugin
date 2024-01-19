package misc

import antlr4.mysql.MySqlParserBaseVisitor

import scala.collection.mutable

class MySqlTablesExtractor extends MySqlParserBaseVisitor[String] {
  private val tablesMap: mutable.Map[String, Int] =
    scala.collection.mutable.HashMap[String, Int]()
  override def visitTableName(
      ctx: antlr4.mysql.MySqlParser.TableNameContext
  ): String = {
    val dbtb: String = ctx.fullId().getText
    if (tablesMap.contains(dbtb)) {
      tablesMap(dbtb) += 1
    } else {
      tablesMap(dbtb) = 1
    }
    null
  }
  def plot(): List[String] = {
    tablesMap.keys.toList
  }
}
