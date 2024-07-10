package action.extract

import doris.{DorisParser, DorisParserBaseVisitor}

import scala.collection.mutable

class DorisTablesExtractor extends DorisParserBaseVisitor[String] {
  private val tablesMap: mutable.Map[String, Int] =
    scala.collection.mutable.HashMap[String, Int]()

  private val targetTables: mutable.Set[String] = mutable.Set[String]()

  override def visitInsertTable(ctx: DorisParser.InsertTableContext): String = {
    val targetTableName = ctx.multipartIdentifier().getText
    targetTables += targetTableName
    visitChildren(ctx)
  }

  override def visitTableName(ctx: DorisParser.TableNameContext): String = {
    val dbtb: String = ctx.multipartIdentifier().getText
    if (tablesMap.contains(dbtb)) {
      tablesMap(dbtb) += 1
    } else {
      tablesMap(dbtb) = 1
    }
    null
  }
  def plot(): List[(String, Int)] = {
    tablesMap.toList
  }

  def getTargetTables(): Set[String] = {
    targetTables.toSet
  }
}
