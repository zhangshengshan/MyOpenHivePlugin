package action.extract

import doris.{DorisParser, DorisParserBaseVisitor}

import scala.collection.mutable

class DorisTablesExtractor extends DorisParserBaseVisitor[String] {
  private val tablesMap: mutable.Map[String, Int] =
    scala.collection.mutable.HashMap[String, Int]()

  private val targetTableSourceTablesMap
      : mutable.Map[String, mutable.ListBuffer[String]] =
    scala.collection.mutable.HashMap[String, mutable.ListBuffer[String]]()

  private val targetTableAliasTablesFilter
      : mutable.Map[String, mutable.ListBuffer[String]] =
    scala.collection.mutable.HashMap[String, mutable.ListBuffer[String]]()
  private val targetTables: mutable.Set[String] = mutable.Set[String]()

  private val aliasTables = mutable.Set[String]()

  private var curInsertTable: Option[String] = None

  override def visitInsertTable(ctx: DorisParser.InsertTableContext): String = {
    val targetTableName = ctx.multipartIdentifier().getText.replaceAll("`", "")
    targetTables += targetTableName
    curInsertTable = Some(targetTableName)
    visitChildren(ctx)
  }

  override def visitTableName(ctx: DorisParser.TableNameContext): String = {
    val dbtb: String = ctx.multipartIdentifier().getText.replaceAll("`", "")
    if (tablesMap.contains(dbtb)) {
      tablesMap(dbtb) += 1
    } else {
      tablesMap(dbtb) = 1
    }
    if (curInsertTable.isDefined) {
      if (targetTableSourceTablesMap.contains(curInsertTable.get)) {
        targetTableSourceTablesMap(curInsertTable.get) += dbtb
      } else {
        targetTableSourceTablesMap(curInsertTable.get) =
          mutable.ListBuffer(dbtb)
      }
    } else {
      if (targetTableSourceTablesMap.contains("其他未知")) {
        targetTableSourceTablesMap("其他未知") += dbtb
      } else {
        targetTableSourceTablesMap("其他未知") = mutable.ListBuffer(dbtb)
      }
    }
    null
  }

  override def visitAliasQuery(ctx: DorisParser.AliasQueryContext): String = {
    val aliasTableName = ctx.identifier().getText
    aliasTables add aliasTableName
    if (curInsertTable.isDefined) {
      if (targetTableAliasTablesFilter.contains(curInsertTable.get)) {
        targetTableAliasTablesFilter(curInsertTable.get) += aliasTableName
      } else {
        targetTableAliasTablesFilter(curInsertTable.get) =
          mutable.ListBuffer(aliasTableName)
      }
    }
    super.visitAliasQuery(ctx)
  }

  override def visitAliasQuery(ctx: DorisParser.AliasQueryContext): String = {
    val aliasTableName = ctx.identifier().getText
    aliasTables add aliasTableName
    if (curInsertTable.isDefined) {
      if (targetTableAliasTablesFilter.contains(curInsertTable.get)) {
        targetTableAliasTablesFilter(curInsertTable.get) += aliasTableName
      } else {
        targetTableAliasTablesFilter(curInsertTable.get) =
          mutable.ListBuffer(aliasTableName)
      }
    }
    super.visitAliasQuery(ctx)
  }

  def plot(): List[(String, Int)] = {
    tablesMap.filter(x => !aliasTables.contains(x._1)).toList
  }

  def getTargetTables(): Set[String] = {
    targetTables.toSet
  }

  def getDepedence: List[(String, String)] = {
    val iterable = targetTableSourceTablesMap
      .map(x => {
        val target = x._1
        val source = x._2.toList
        source
          .filter(item => {
            !targetTableAliasTablesFilter.contains(
              target
            ) || !targetTableAliasTablesFilter(target).contains(item)
          })
          .map(i => {
            (target, i)
          })
      })
      .flatten
    iterable.toList
  }
}
