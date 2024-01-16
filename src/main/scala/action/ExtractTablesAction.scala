package action

import antlr.g4.{SqlBaseLexer, SqlBaseParser}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil
import mysql.{MySqlLexer, MySqlParser, MySqlParserBaseListener}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

class ExtractTablesAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val editor: Editor = event.getData(CommonDataKeys.EDITOR)


    val file: PsiFile = event.getData(CommonDataKeys.PSI_FILE)
    val fileType = file.getFileType.getName

    println(fileType)


    val text: String = editor.getDocument.getText


    // TODO: duplicated code to be refactored



    fileType match {
      case "Hive File" => {
        processHiveTables(text)
      }
      case "SQL" => {
        processMySQLTables(text)
      }
    }


  }

  def processHiveTables(text:String): Unit = {
    val lexer: SqlBaseLexer =
      new SqlBaseLexer(
        new CaseChangingCharStream(CharStreams.fromString(text), true)
      )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser: SqlBaseParser = new SqlBaseParser(commonTokenStream)
    val context: SqlBaseParser.StatementContext = parser.statement()
    val visitor = new SparkSqlTablesExtractVisitor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()
    if (MyConfigurable.getInstance().isDownloadAfterExtract) {

    }
    Messages.showInfoMessage(
      plot.mkString("\r"),
      "Extracted Tables"
    )
    ClipBoardUtil.copyToClipBoard(plot.mkString("\r"))
  }
  private def processMySQLTables(text: String): Unit = {
    println("SQL")
    val lexer = new MySqlLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser = new MySqlParser(commonTokenStream)
    val context: MySqlParser.RootContext = parser.root()
    val visitor = new MySQLTablesExtractor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()

    if (MyConfigurable.getInstance().isDownloadAfterExtract) {

    }
    Messages.showInfoMessage(
      plot.mkString("\r"),
      "Extracted Tables"
    )
  }
}
