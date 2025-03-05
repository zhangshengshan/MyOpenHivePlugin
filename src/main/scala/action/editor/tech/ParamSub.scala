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

    // 在一个写命令动作中执行替换操作，确保线程安全
    WriteCommandAction.runWriteCommandAction(
      project,
      new Runnable {
        override def run(): Unit = {
          try {
            // 把每一行的 #{ 给替换为 ${

            val selectedText = document.getText(new TextRange(start, end))
            // 将 #{ 替换为 ${

            // 弹出选择框，选择把#{替换为${ 还是 ${ 替换为 ${}
            // 这里可以使用一个对话框来让用户选择

            val choice = Messages.showYesNoDialog(
              project,
              "#{替换为${ 还是 ${ 替换为 ${} ?",
              "替换",
              Messages.getQuestionIcon
            )
            val replacedText = if (choice == Messages.YES) {
//              selectedText.replace("#{", "\"${").replace("}", "}\"")
              selectedText.replace("#{", "${")
            } else {
//              selectedText.replace("\"${", "#{").replace("}\"", "}")
              selectedText.replace("${", "#{")
            }
            // 将替换后的文本写回文档
            document.replaceString(start, end, replacedText)

          } catch {
            case e: Throwable =>
              ExceptionHandle.handleException(e)

          }
          Messages.showInfoMessage(project, "SUCESS", "替换成功")
        }
      }
    )
  }
}
