package action.extract

import doris.{DorisParser, DorisParserBaseVisitor}

import scala.collection.mutable

class DorisTablesExtractor extends DorisParserBaseVisitor[String] {
  private val tablesMap: mutable.Map[String, Int] =
    scala.collection.mutable.HashMap[String, Int]()

  private val targetTables: mutable.Set[String] = mutable.Set[String]()

  private val aliasTables = mutable.Set[String]()

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

  override def visitAliasQuery(ctx: DorisParser.AliasQueryContext): String = {
    val aliasTableName = ctx.identifier().getText
    aliasTables add aliasTableName
    super.visitAliasQuery(ctx)
  }

  def plot(): List[(String, Int)] = {
    tablesMap.filter(x => !aliasTables.contains(x._1)).toList
  }

  def getTargetTables(): Set[String] = {
    targetTables.toSet
  }

  def getDepedence: List[(String, String)] = {
    getTargetTables().flatMap(target => plot().map(x => (target, x._1))).toList
  }
}
