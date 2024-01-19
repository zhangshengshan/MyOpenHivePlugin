package misc

import action.SparkSqlTablesExtractVisitor
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
    val visitor = new SparkSqlTablesExtractVisitor()
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
    val lexer = new DorisLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser = new DorisParser(commonTokenStream)
    val context: DorisParser.MultiStatementsContext = parser.multiStatements()
    val visitor = new DorisTablesExtractor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()

    if (MyConfigurable.getInstance().isDownloadAfterExtract) {}
    Messages.showInfoMessage(
      plot.mkString("\r"),
      "Extracted Tables"
    )
    ClipBoardUtil.copyToClipBoard(plot.mkString("\r"))
  }

  def processMySQLTables(text: String): Unit = {
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
      plot.mkString(System.lineSeparator()) ,
      "Extracted Tables"
    )
    ClipBoardUtil.copyToClipBoard(plot.mkString(System.lineSeparator()))
  }
}
