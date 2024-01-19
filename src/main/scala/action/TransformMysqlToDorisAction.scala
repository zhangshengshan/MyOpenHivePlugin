package action

import cache.ExpiringCache
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import hierachyconfig.MyConfigurable

import scala.util.matching.Regex

class TransformMysqlToDorisAction extends AnAction {

  override def actionPerformed(e: AnActionEvent): Unit = {

    val value: MyConfigurable = MyConfigurable.getInstance()
    val host = value.getHost
    val port = value.getPort
    val user = value.getUser
    val jdbcUrl = value.getJdbcURL

    val password = value.getPassword
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)

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

    val expandObj: String = editor.getDocument.getText(
      new TextRange(leftOffset, rightOffset)
    )
    Messages.showInfoMessage(expandObj, "get table from current cursor")

    // if expandObj is db.tb then get the tb if expandObj is tb then get tb
    val Pattern = "(.*\\.)?(.*)".r
    val searchTableName = expandObj match {
      case Pattern(_, yyy) => yyy
    }

    // 在这里写一段slick查询每一个数据中都有哪些数据表的代码
    import slick.jdbc.MySQLProfile.api._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Await
    import scala.concurrent.duration.Duration

    val dbConfig = Database.forURL(
      url = jdbcUrl,
      user = s"$user",
      password = s"$password",
      driver = "com.mysql.cj.jdbc.Driver"
    )

    val showDatabasesAction = sql"SHOW DATABASES".as[String]
    val showDatabasesFuture = dbConfig.run(showDatabasesAction)
    val databases = Await.result(showDatabasesFuture, Duration.Inf)

    val dorisTableList = new scala.collection.mutable.ListBuffer[String]()
    databases.foreach { database =>
      val showTablesAction = sql"SHOW TABLES IN #$database LIKE '%#$searchTableName%' ".as[String]
      val showTablesFuture = dbConfig.run(showTablesAction)
      val tables = Await.result(showTablesFuture, Duration.Inf)

      tables.foreach(table => {
        println(s"Table: $table")
        dorisTableList.append(s"$database.$table")
      })

    }

    if(dorisTableList.isEmpty) {
      Messages.showInfoMessage("No table found", "Error")
      return
    }

    if(dorisTableList.size >= 5 ) {
        Messages.showInfoMessage("Too many tables found, please input more specific table name", "Error")
        return
    }

    // PopUp a dialog to choose the tabledialog to choose the table
    val choosedTable = Messages.showEditableChooseDialog(
      "Choose the table",
      "Choose the table",
      Messages.getInformationIcon,
      dorisTableList.toArray,
      dorisTableList.head,
      null
    )
    println(choosedTable)

    // substitude the search table with the choosed table
    ApplicationManager.getApplication.runWriteAction(new Runnable {
      override def run(): Unit = {
        // 获取Document对象
        val document = editor.getDocument
        // 获取文档的文本
        val originalText = document.getText()
        // 替换所有出现的searchTableName为choosedTable
        val searchQuoteName = Regex.quote(searchTableName)
        // searchTableName 两边有空白字符才进行替换
        val newText = originalText.replaceAll("(?<=\\s|^)" + searchQuoteName + "(?=\\s|$)", choosedTable)
        // 在Document对象中替换整个文本
        document.setText(newText)
      }
    })
    // 重新格式化代码
    Messages.showInfoMessage("TransformMysqlToDorisAction Completed", "Information")
  }
}
