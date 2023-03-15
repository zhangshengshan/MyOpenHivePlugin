package action

import antlr.g4.{SqlBaseParser, SqlBaseParserBaseVisitor}

class SparkSqlTablesExtractVisitor extends SqlBaseParserBaseVisitor[String] {
  val tablesMap = scala.collection.mutable.HashMap[String, Int]()

  override def visitTableName(ctx: SqlBaseParser.TableNameContext): String = {
    val dbtb: String = ctx.getText
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
