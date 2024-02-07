package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.ui.Messages

class CommentProcess extends AnAction("注释处理") {
  override def actionPerformed(e: AnActionEvent): Unit = {

    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument
    val text: String = document.getText
    val newText: String = text
      .replaceAll("--", " -- ")
    try {
      // write the new text to the editor and save
      ApplicationManager.getApplication.runWriteAction(new Runnable {
        override def run(): Unit = {
          document.setText(newText)
          editor.getCaretModel.moveToOffset(0)
        }
      })
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        Messages.showMessageDialog(
          e.getMessage,
          "Error",
          Messages.getErrorIcon
        )
    }
  }
}
