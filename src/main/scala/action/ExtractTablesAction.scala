package action

import action.linage.PlotUtil
import antlr.g4.{SqlBaseLexer, SqlBaseParser}
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import doris.{DorisLexer, DorisParser}
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil
import misc.TableExtractUtil.processHiveTables
import misc.TableExtractUtil.processDorisTables
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

class ExtractTablesAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val editor: Editor = event.getData(CommonDataKeys.EDITOR)

    val file: PsiFile = event.getData(CommonDataKeys.PSI_FILE)
    val fileType = file.getFileType.getName
    println(fileType)
    val text: String = editor.getDocument.getText
    fileType match {
      case "Hive File" => {
        processHiveTables(text)
      }
      case "SQL" => {
        val tuples: List[(String, String)] = processDorisTables(text)
        PlotUtil.plotAction(editor, tuples)
      }
    }
  }

}
