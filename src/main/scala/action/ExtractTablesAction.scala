package action

import antlr.g4.{SqlBaseLexer, SqlBaseParser}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

class ExtractTablesAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val editor: Editor = event.getData(CommonDataKeys.EDITOR)

    val text = editor.getDocument.getText
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
}
