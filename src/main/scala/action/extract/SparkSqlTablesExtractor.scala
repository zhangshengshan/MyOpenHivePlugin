package action.extract

import antlr.g4.{SqlBaseParser, SqlBaseParserBaseVisitor}

import scala.collection.mutable

class SparkSqlTablesExtractor extends SqlBaseParserBaseVisitor[String] {
  private val tablesMap: mutable.Map[String, Int] = scala.collection.mutable.HashMap[String, Int]()

  override def visitTableName(ctx: SqlBaseParser.TableNameContext): String = {
    val dbtb: String = ctx.multipartIdentifier().getText
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
