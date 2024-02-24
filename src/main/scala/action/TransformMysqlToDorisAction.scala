package action

import cache.CacheUtil
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil
import misc.TableExtractUtil.processMySQLTables
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.matching.Regex

class TransformMysqlToDorisAction extends AnAction {

  override def actionPerformed(e: AnActionEvent): Unit = {

    val successSubstitude = new scala.collection.mutable.ListBuffer[String]()

    val value: MyConfigurable = MyConfigurable.getInstance()
    val host = value.getHost
    val port = value.getPort
    val user = value.getUser
    val jdbcUrl = value.getJdbcURL

    val password = value.getPassword
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document = editor.getDocument

    val model = editor.getCaretModel
    val offset = model.getOffset
    val project: Project = editor.getProject
    var leftOffset = offset
    var rightOffset = offset

    // 判断是空格还是换行符,或者\t
    Character.isWhitespace(
      editor.getDocument.getText.charAt(offset - 1)
    )

    while (
      leftOffset > 0 && !Character.isWhitespace(
        editor.getDocument.getText.charAt(leftOffset - 1)
      )
    ) {
      leftOffset = leftOffset - 1
    }
    while (
      rightOffset < editor.getDocument.getTextLength && !Character.isWhitespace(
        editor.getDocument.getText.charAt(rightOffset)
      )
    ) {
      rightOffset = rightOffset + 1
    }
    val sourceTables: List[String] = processMySQLTables(document.getText)

    val expandObj: String = editor.getDocument.getText(
      new TextRange(leftOffset, rightOffset)
    )
    // if expandObj is db.tb then get the tb if expandObj is tb then get tb
    val Pattern = "(.*\\.)?(.*)".r
    val searchTableName = expandObj match {
      case Pattern(_, yyy) => yyy
    }

    // 在这里写一段slick查询每一个数据中都有哪些数据表的代码

    val dbConfig = Database.forURL(
      url = jdbcUrl,
      user = s"$user",
      password = s"$password",
      driver = "com.mysql.cj.jdbc.Driver"
    )

    def replaceMysqlWithDoris(searchTableName: String): Unit = {
      val dorisTableList = new scala.collection.mutable.ListBuffer[String]()
      var choosedTable: String = null

      // if cache hit then get the table from cache
      def chooseTargetTable(sourceTable: String): String = {
        val init = if (dorisTableList.isEmpty) return null

        val msg = if (dorisTableList.size == 1) s" ONLY ONE TABLE FOUND FOR $sourceTable"
        else s" CHOOSE THE TABLE FOR $sourceTable"

        val icon = if (dorisTableList.size == 1) Messages.getErrorIcon
        else Messages.getQuestionIcon

        val tableName = Messages.showEditableChooseDialog(
          msg,
          "CHOOSE THE TABLE",
          icon,
          dorisTableList.toArray,
          dorisTableList.head,
          null
        )
        tableName
      }
      if (CacheUtil.cache.isEmpty()) {
        val showDatabasesAction = sql"SHOW DATABASES".as[String]
        val showDatabasesFuture = dbConfig.run(showDatabasesAction)
        val databases = Await.result(showDatabasesFuture, Duration.Inf)
        databases.foreach { database =>
          if (
            database != "information_schema" && database != "mysql" && database != "performance_schema" && database != "sys"
          ) {
            val showTablesAction = sql"SHOW TABLES IN #$database".as[String]
            val showTablesFuture = dbConfig.run(showTablesAction)
            val tables = Await.result(showTablesFuture, Duration.Inf)
            tables.foreach((table: String) => {
              CacheUtil.cache.put(s"$database.$table", s"$database.$table")
              if (table.contains(searchTableName)) {
                dorisTableList.append(s"$database.$table")
              }
            })
          }
        }
        choosedTable = chooseTargetTable(searchTableName)
      } else {
        if (CacheUtil.cache.exists(searchTableName)) {
          CacheUtil.cache
            .getSimilar(searchTableName)
            .foreach(table => {
              dorisTableList.append(table)
            })
          // choose file from dorisTableList
          choosedTable = chooseTargetTable(searchTableName)
          if (choosedTable == null) {
            dorisTableList.clear()
            val showDatabasesAction = sql"SHOW DATABASES".as[String]
            val showDatabasesFuture = dbConfig.run(showDatabasesAction)
            val databases = Await.result(showDatabasesFuture, Duration.Inf)
            databases.foreach { database =>
              if (
                database != "information_schema" && database != "mysql" && database != "performance_schema" && database != "sys"
              ) {
                // if cache is empty then get all the tables
                val showTablesAction =
                  sql"SHOW TABLES IN #$database LIKE '%#$searchTableName%' "
                    .as[String]
                val showTablesFuture = dbConfig.run(showTablesAction)
                val tables = Await.result(showTablesFuture, Duration.Inf)
                tables.foreach(table => {
                  CacheUtil.cache.put(s"$database.$table", s"$database.$table")
                  dorisTableList.append(s"$database.$table")
                })
              }
            }
            choosedTable = chooseTargetTable(searchTableName)
          }
        } else {
          val showDatabasesAction = sql"SHOW DATABASES".as[String]
          val showDatabasesFuture = dbConfig.run(showDatabasesAction)
          val databases = Await.result(showDatabasesFuture, Duration.Inf)
          databases.foreach { database =>
            if (
              database != "information_schema" && database != "mysql" && database != "performance_schema" && database != "sys"
            ) {
              val showTablesAction = sql"SHOW TABLES IN #$database".as[String]
              val showTablesFuture = dbConfig.run(showTablesAction)
              val tables = Await.result(showTablesFuture, Duration.Inf)
              tables.foreach((table: String) => {
                CacheUtil.cache.put(s"$database.$table", s"$database.$table")
                if (table.contains(searchTableName)) {
                  dorisTableList.append(s"$database.$table")
                }
              })
            }
          }
          choosedTable = chooseTargetTable(searchTableName)
        }
      }
      if (choosedTable != null) {
        // here record the sourcetable and target table
        successSubstitude.append(s"-- $searchTableName -> $choosedTable")
        // substitude the search table with the choosed table
        ApplicationManager.getApplication.runWriteAction(new Runnable {

          override def run(): Unit = {

            // 获取文档的文本
            val originalText = document.getText()
            // 替换所有出现的searchTableName为choosedTable
            val searchQuoteName = Regex.quote(searchTableName)
            // searchTableName 两边有空白字符才进行替换
            val newText = originalText.replaceAll(
              "(?<=\\s|^)" + searchQuoteName + "(?=\\s|$)",
              choosedTable
            )
            // 在Document对象中替换整个文本
            document.setText(newText)
          }
        })
        Messages.showInfoMessage(
          "TransformMysqlToDorisAction Completed",
          "Information"
        )
      } else {
        Messages.showInfoMessage(
          s"Doris Table ${searchTableName} Not Found!",
          "Information"
        )
      }
    }
    sourceTables.foreach(table => {
      replaceMysqlWithDoris(table)
    })

    Messages.showInfoMessage(
      successSubstitude.mkString(System.lineSeparator()),
      "TransformMysqlToDorisAction Completed"
    )
    ClipBoardUtil.copyToClipBoard(successSubstitude.mkString(System.lineSeparator()))
  }
}
