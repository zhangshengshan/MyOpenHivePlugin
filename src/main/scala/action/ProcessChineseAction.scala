package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.{Application, ApplicationManager}
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.ui.Messages
import misc.ClipBoardUtil

class ProcessChineseAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {

    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument
    val text: String = document.getText
    val newText: String = text
      .replaceAll("\\.\"([\\u4e00-\\u9fa5]+)\"", ".`$1`")
      .replaceAll("\\.([\\u4e00-\\u9fa5]+)", ".`$1`")
      .replaceAll("(AS|as)\\s+([\\u4e00-\\u9fa5]+)", "$1 `$2`")
      .replaceAll("\\.\"([^\"]+)\"", ".$1")

    Messages.showMessageDialog(
      newText,
      "Done",
      Messages.getInformationIcon
    )
    ClipBoardUtil.copyToClipBoard(newText)

    try {
      // write the new text to the editor and save
      ApplicationManager.getApplication.runWriteAction(new Runnable {
        override def run(): Unit = {
          document.setText(newText)
          editor.getCaretModel.moveToOffset(0)
        }
      })
    } catch {
      case e => {
        e.printStackTrace()
        Messages.showMessageDialog(
          e.getMessage,
          "Error",
          Messages.getErrorIcon
        )
      }
    }
    return
  }
}

