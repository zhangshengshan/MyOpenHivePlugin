package action

import antlr4.dsl.MyDSLLexer
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import misc.TableExtractUtil.{processDorisTables, processHiveTables}
import org.antlr.v4.runtime.CharStreams
import zss.mysqlparser.CaseChangingCharStream

class DSL2DorisAction extends AnAction {

  override def actionPerformed(event: AnActionEvent): Unit = {

    val editor: Editor = event.getData(CommonDataKeys.EDITOR)

    val file: PsiFile = event.getData(CommonDataKeys.PSI_FILE)
    val fileType = file.getFileType.getName
    println(fileType)
    val text: String = editor.getDocument.getText
    println(text)

    val lexer = new MyDSLLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )
    val commonTokenStream = new org.antlr.v4.runtime.CommonTokenStream(lexer)
    val parser = new antlr4.dsl.MyDSLParser(commonTokenStream)
    val context = parser.root()
    val visitor = new Dsl2SQLGerater()

    visitor.visit(context)
    Messages.showMessageDialog(
      visitor.ret,
      "Information",
      Messages.getInformationIcon
    )

  }
}
