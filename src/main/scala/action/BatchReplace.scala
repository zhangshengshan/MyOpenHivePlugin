package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

import scala.util.matching.Regex

class BatchReplace extends AnAction {

  final val NORMAL_MODE: String = "普通替换"
  final val REGEX_MODE: String = "正则替换"

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

    val subMode: String = Messages.showEditableChooseDialog(
      "请选择替换模式",
      "替换模式",
      Messages.getQuestionIcon,
      Array(NORMAL_MODE, REGEX_MODE),
      NORMAL_MODE,
      null
    )

    lines
      .split("\n")
      .foreach(line => {
        val source: String = line.split("\\|")(0)
        val target: String = line.split("\\|")(1)
        val text: String = document.getText

        if (subMode == NORMAL_MODE) {
          try {
            val newText: String = text
              .replaceAll("\\s+" + source + "\\s+", target)
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
        } else {
          try {
            val r: Regex = source.r
            val str: String = r.replaceAllIn(
              text,
              (m: Regex.Match) => {
                target
              }
            )
            // write the new text to the editor and save
            ApplicationManager.getApplication.runWriteAction(new Runnable {
              override def run(): Unit = {
                document.setText(str)
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
        }
      })

  }
}
