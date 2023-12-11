package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class BatchReplace extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {

    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument

    //此处弹出多行对话框
    val lines: String = Messages.showMultilineInputDialog(
      project,
      "source|target",
      "批量替换",
      null,
      Messages.getQuestionIcon,
      null
    )

    lines
      .split("\n")
      .foreach(line => {

        val source: String = line.split("\\|")(0)
        val target: String = line.split("\\|")(1)
        val text: String = document.getText
        val newText: String = text
          .replaceAll(source, target)
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
      })

  }
}
