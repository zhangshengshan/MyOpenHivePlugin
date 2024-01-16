package action

import mysql.{MySqlParser, MySqlParserBaseVisitor}

import scala.collection.mutable

class MySQLTablesExtractor extends MySqlParserBaseVisitor[String]{

  private val tablesMap: mutable.Map[String, Int] = scala.collection.mutable.HashMap[String, Int]()
  override def visitTableName(ctx: MySqlParser.TableNameContext): String = {
    val dbtb: String = ctx.getText
    if(tablesMap.contains(dbtb)){
      tablesMap(dbtb) += 1
    }else{
        tablesMap(dbtb) = 1
    }
    null
  }
    def plot(): List[String] = {
        tablesMap.keys.toList
    }
}
