package misc

import action.extract.{
  DorisTablesExtractor,
  MySqlTablesExtractor,
  SparkSqlTablesExtractor
}
import antlr.g4.{SqlBaseLexer, SqlBaseParser}
import antlr4.mysql.{MySqlLexer, MySqlParser}
import com.intellij.openapi.ui.Messages
import doris.{DorisLexer, DorisParser}
import hierachyconfig.MyConfigurable
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

object TableExtractUtil {

  def processHiveTables(text: String): Unit = {
    val lexer: SqlBaseLexer =
      new SqlBaseLexer(
        new CaseChangingCharStream(CharStreams.fromString(text), true)
      )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser: SqlBaseParser = new SqlBaseParser(commonTokenStream)
    val context: SqlBaseParser.StatementContext = parser.statement()
    val visitor = new SparkSqlTablesExtractor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()
    if (MyConfigurable.getInstance().isDownloadAfterExtract) {}
    Messages.showInfoMessage(
      plot.mkString("\r"),
      "Extracted Tables"
    )
    ClipBoardUtil.copyToClipBoard(plot.mkString("\r"))
  }
  def processDorisTables(text: String): Unit = {
    println("SQL")

    // delete the line that contains "truncate"
    val lines = text.split("\n")
    val newLines =
      lines.filterNot(x => x.contains("truncate") || x.contains("TRUNCATE"))
    val newText = newLines.mkString("\n")

    val lexer = new DorisLexer(
      new CaseChangingCharStream(CharStreams.fromString(newText), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser = new DorisParser(commonTokenStream)
    val context: DorisParser.MultiStatementsContext = parser.multiStatements()
    val visitor = new DorisTablesExtractor()

    visitor.visit(context)

    val plot: List[(String, Int)] = visitor.plot()

    val targetTables = visitor.getTargetTables().mkString("tables are:","\r", "!")
    Messages.showInfoMessage(
        targetTables,
        "Extracted Tables"
    )

    if (MyConfigurable.getInstance().isDownloadAfterExtract) {}

    Messages.showInfoMessage(
      plot.map(x => x._1 + ":" + x._2).mkString("\r"),
      "Extracted Tables"
    )
    Messages.showYesNoDialog(
      "Tables And Frequency Or Only Tables?",
      "Copy to Clipboard",
      Messages.getQuestionIcon
    ) match {
      case Messages.YES =>
        ClipBoardUtil.copyToClipBoard(plot.map(x => x._1 + ":" + x._2).mkString("\r"))
      case Messages.NO =>
        ClipBoardUtil.copyToClipBoard(plot.map(x => x._1).mkString("\r"))
    }


//    Save target table and dependecies into excel file
//    val excel = new ExcelUtil()
//    excel.saveToExcel(plot, targetTables)











  }

  def processMySQLTables(text: String) = {
    val lexer = new MySqlLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser = new MySqlParser(commonTokenStream)

    val context: MySqlParser.SqlStatementsContext = parser.sqlStatements()
    val visitor = new MySqlTablesExtractor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()

    if (MyConfigurable.getInstance().isDownloadAfterExtract) {}
    Messages.showInfoMessage(
      plot.mkString(System.lineSeparator()),
      "Extracted Tables"
    )
    plot
  }
}
