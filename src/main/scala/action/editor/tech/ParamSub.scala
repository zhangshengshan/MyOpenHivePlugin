package action.editor.tech

import action.util.ExceptionHandle
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.{Document, Editor, SelectionModel}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange

/** SelectPartsAsAction 类用于处理将编辑器中选中的部分代码转换为带有 AS 关键字的格式
  * 它通过覆盖 actionPerformed 方法来实现这一功能
  * selectedText.replaceAll("#\\{([^}]*)}", "\\${$1}")
  *      } else {
  *        selectedText.replaceAll("\\$\\{([^}]*)}", "#{$1}")
  */
class ParamSub extends AnAction {

  /** 当动作触发时执行的方法
    * 它会获取当前选中的文本，并在每行的行首添加 -- 进行注释
    * 然后提供一个可编辑的选择对话框，让用户选择替换模式
    * 最后，对选中的代码部分进行替换操作
    *
    * @param e AnActionEvent 类型的事件，包含操作的上下文信息，如当前项目和编辑器
    */
  override def actionPerformed(e: AnActionEvent): Unit = {

    // 获取项目和编辑器实例
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)

    // 获取选中文字的起始和结束位置
    val selectionModel: SelectionModel = editor.getSelectionModel
    val start: Int = selectionModel.getSelectionStart
    val end: Int =
      if (selectionModel.getSelectionEnd >= 1)
        selectionModel.getSelectionEnd - 1
      else selectionModel.getSelectionEnd
    val document: Document = editor.getDocument

    // 获取选中文字所在行的起始和结束行号
    val lineStart: Int = document.getLineNumber(start)
    val lineEnd: Int = document.getLineNumber(end)

    val transform_flag = Messages.showYesNoDialog(
      project,
      s" #{xxx} => ${} 请按Y \n  ${} => #{xxx} 请按N",
      "替换参数名",
      Messages.getQuestionIcon()
    )

    // 在一个写命令动作中执行替换操作，确保线程安全
    WriteCommandAction.runWriteCommandAction(
      project,
      new Runnable {
        override def run(): Unit = {
          try {
            // 对每一行进行处理，添加 -- 注释，并进行替换操作
            lineStart to lineEnd foreach { curLine: Int =>
              {
                val insertPos: Int = document.getLineStartOffset(curLine)
                val lineText: String = document.getText(
                  new TextRange(
                    document.getLineStartOffset(curLine),
                    document.getLineEndOffset(curLine)
                  )
                )

                val finalResult = if (transform_flag == Messages.YES) {
                  // 把  <if test="xxxxxxxxxxx">  这种行
                  // 或者 </if>  进行删除
                  lineText.replaceAll("#\\{([^}]*)}", "'\\${$1}\'").replaceAll("<if test=\"([^>]*)\">", "").replaceAll("</if>", "")
                } else {
                  lineText.replaceAll("\'*\\$\\{([^}]*)}\'*", "#{$1}")
                }
                // 定义匹配模式的正则表达式
                // 将替换后的结果写入文档
                document.replaceString(
                  document.getLineStartOffset(curLine),
                  document.getLineEndOffset(curLine),
                  finalResult
                )

              }
            }
          } catch {
            case e: Throwable =>
              ExceptionHandle.handleException(e)

          }
        }
      }
    )
    // 显示替换成功的消息
    Messages.showInfoMessage(project, "SUCCESS", "替换成功")
  }
}
