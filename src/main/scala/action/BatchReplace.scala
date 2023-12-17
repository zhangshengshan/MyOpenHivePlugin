package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import misc.ClipBoardUtil

import scala.util.matching.Regex

class BatchReplace extends AnAction {

  private final val NORMAL_MODE: String = "普通替换"
  private final val REGEX_MODE: String = "正则替换"
  private final val ANOTHER_EXTRACT: String = "另一种提取方式"

  override def actionPerformed(e: AnActionEvent): Unit = {

    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument

    //此处弹出多行对话框
    val lines: String = Messages.showMultilineInputDialog(
      project,
      "source|target或者regex",
      "批量替换",
      null,
      Messages.getQuestionIcon,
      null
    )

    val subMode: String = Messages.showEditableChooseDialog(
      "请选择替换模式",
      "替换模式",
      Messages.getQuestionIcon,
      Array(NORMAL_MODE, REGEX_MODE, ANOTHER_EXTRACT),
      NORMAL_MODE,
      null
    )

    val anotherStringBuffer: StringBuffer = new StringBuffer()

    lines
      .split("\n")
      .foreach(line => {

        if (subMode == NORMAL_MODE) {
          val source: String = line.split("\\|")(0)
          val target: String = line.split("\\|")(1)
          val text: String = document.getText
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
        } else if (subMode == REGEX_MODE) {
          val source: String = line.split("\\|")(0)
          val target: String = line.split("\\|")(1)
          val text: String = document.getText
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
        } else {
//          val r: Regex = (line + "\\.[^ ]+").r
          val r: Regex = line.r
          val text: String = document.getText
          r.findAllMatchIn(text)
            .foreach(m => {
              anotherStringBuffer.append(m.toString() + "\n")
            })
        }
      })
    if (anotherStringBuffer.length() > 0) {
      Messages.showInfoMessage(anotherStringBuffer.toString(), "提取结果")
      ClipBoardUtil.copyToClipBoard(anotherStringBuffer.toString())
    }
  }
}
