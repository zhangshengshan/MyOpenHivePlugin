package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.{DialogWrapper, Messages}
import misc.ClipBoardUtil

import javax.swing.JComponent
import scala.util.matching.Regex

class BatchReplace extends AnAction {

  /** 普通替换模式
    */
  private final val NORMAL_MODE: String = "普通替换"

  /** 正则替换模式
    */
  private final val REGEX_MODE: String = "正则替换"

  /** 另一种提取方式
    */
  private final val ANOTHER_EXTRACT: String = "另一种提取方式"


  private final val CLIPBOARD_WRAP: String = "剪切板包裹"



  /** 处理动作触发时的事件
    *
    * @param e 动作事件
    */
  override def actionPerformed(e: AnActionEvent): Unit = {


    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument

    // 弹出多行对话框获取替换的源字符串和目标字符串
    val lines: String = Messages.showMultilineInputDialog(
      project,
      "源字符串|目标字符串",
      "批量替换",
      null,
      Messages.getQuestionIcon,
      null
    )

    // 弹出可编辑选择对话框选择替换模式
    val subMode: String = Messages.showEditableChooseDialog(
      "请选择替换模式",
      "替换模式",
      Messages.getQuestionIcon,
      Array(NORMAL_MODE, REGEX_MODE, ANOTHER_EXTRACT, CLIPBOARD_WRAP),
      NORMAL_MODE,
      null
    )

    val anotherStringBuffer: StringBuffer = new StringBuffer()

    if(subMode == CLIPBOARD_WRAP) {
      val clipboard = ClipBoardUtil.getFromClipboard
      val str = clipboard.split("\n").map(x => "'" + x.strip() + "'").mkString("(", ",", ")")
      Messages.showInfoMessage(str, "剪切板内容")
      ClipBoardUtil.copyToClipBoard(str)

    } else {

      // 对每一行进行处理
      lines
        .split("\n")
        .foreach((line: String) => {

          if (subMode == NORMAL_MODE) {
            // 获取源字符串和目标字符串
            val source: String = line.split("\\|")(0)
            val target: String = line.split("\\|")(1)
            // 获取编辑器中的文本
            val text: String = document.getText
            try {
              // 使用正则表达式替换文本中的源字符串为目标字符串
              val newText: String = text
                .replaceAll("\\s+" + source + "\\s+", target)
              // 将新文本写入编辑器并保存
              ApplicationManager.getApplication.runWriteAction(new Runnable {
                override def run(): Unit = {
                  document.setText(newText)
                  editor.getCaretModel.moveToOffset(0)
                }
              })
            } catch {
              case e: Throwable =>
                e.printStackTrace()
                // 弹出错误对话框显示错误信息
                Messages.showMessageDialog(
                  e.getMessage,
                  "Error",
                  Messages.getErrorIcon
                )
            }
          } else if (subMode == REGEX_MODE) {
            // 获取源正则表达式和目标字符串
            val source: String = line.split("\\|")(0)
            val target: String = line.split("\\|")(1)
            // 获取编辑器中的文本
            val text: String = document.getText
            try {
              // 使用源正则表达式替换文本中的匹配项为目标字符串
              val r: Regex = source.r
              val str: String = r.replaceAllIn(
                text,
                (m: Regex.Match) => {
                  target
                }
              )
              // 将新文本写入编辑器并保存
              ApplicationManager.getApplication.runWriteAction(new Runnable {
                override def run(): Unit = {
                  document.setText(str)
                  editor.getCaretModel.moveToOffset(0)
                }
              })
            } catch {
              case e: Throwable =>
                e.printStackTrace()
                // 弹出错误对话框显示错误信息
                Messages.showMessageDialog(
                  e.getMessage,
                  "Error",
                  Messages.getErrorIcon
                )
            }
          } else {
            // 获取正则表达式
            val r: Regex = line.r
            val text: String = document.getText
            // 查找匹配项并提取
            r.findAllMatchIn(text)
              .foreach((m: Regex.Match) => {
                anotherStringBuffer.append(m.toString() + "\n")
              })
          }
        })
    }


    // 如果有提取结果，则显示提取结果并复制到剪贴板
    if (anotherStringBuffer.length() > 0) {
      Messages.showInfoMessage(anotherStringBuffer.toString, "提取结果")
      ClipBoardUtil.copyToClipBoard(anotherStringBuffer.toString)
    }
  }
}
