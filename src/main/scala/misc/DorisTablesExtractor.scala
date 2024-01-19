package misc

import doris.{DorisParser, DorisParserBaseVisitor}

import scala.collection.mutable

class DorisTablesExtractor extends DorisParserBaseVisitor[String]{
  private val tablesMap: mutable.Map[String, Int] = scala.collection.mutable.HashMap[String, Int]()

override def visitTableName(ctx: DorisParser.TableNameContext): String = {
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
