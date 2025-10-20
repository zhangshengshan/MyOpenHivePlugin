package action

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

class CommentSelectLinesToggle extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {

    /** 获取编辑器中选中的行，并在行首添加指定模式的前缀
      *
      * @param e 动作事件对象，包含编辑器和项目信息
      * @return 无返回值
      */
    // 获取编辑器选择的行，然后在行首添加
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)

    val selectionModel: SelectionModel = editor.getSelectionModel

    // 获取选中文本的起始和结束位置
    val start: Int = selectionModel.getSelectionStart
    val end: Int =
      if (selectionModel.getSelectionEnd >= 1)
        selectionModel.getSelectionEnd - 1
      else selectionModel.getSelectionEnd
    val document: Document = editor.getDocument

    val lineStart: Int = document.getLineNumber(start)
    val lineEnd: Int = document.getLineNumber(end)

    // 弹出可编辑选择对话框选择替换模式
    val subMode: String = Messages.showEditableChooseDialog(
      "请选择替换模式",
      "替换模式",
      Messages.getQuestionIcon,
      Array("comment", "ddl"),
      "comment",
      null
    )

    // 在 lineStart to lineEnd foreach 循环之前添加如下代码：
    var matchCount = 0
    var totalCount = 0

// 统计匹配和总行数
    lineStart to lineEnd foreach { curLine: Int =>
      totalCount += 1
      val lineText = document.getText(
        new TextRange(
          document.getLineStartOffset(curLine),
          document.getLineEndOffset(curLine)
        )
      )
      if ("^\\s*--.*".r.findFirstIn(lineText).isDefined) {
        matchCount += 1
      }
    }

    val shouldRemoveComments = matchCount > totalCount / 2

    WriteCommandAction.runWriteCommandAction(
      project,
      new Runnable {

        /** 执行注释或取消注释行的操作
          *
          * 该方法遍历指定范围内的所有行，根据shouldRemoveComments标志决定是添加注释还是移除注释
          * 对于每一行，如果是添加注释则在行首插入"-- "，如果是移除注释则删除行首的"--"及其前面的空白字符
          *
          * @param lineStart 起始行号（包含）
          * @param lineEnd 结束行号（包含）
          * @param document 操作的文档对象
          * @param shouldRemoveComments true表示移除注释，false表示添加注释
          * @return Unit 无返回值
          */
        override def run(): Unit = {
          try {

            // 遍历从lineStart到lineEnd的每一行，执行注释/取消注释操作
            lineStart to lineEnd foreach { curLine: Int =>
              val lineText = document.getText(
                new TextRange(
                  document.getLineStartOffset(curLine),
                  document.getLineEndOffset(curLine)
                )
              )
              if (shouldRemoveComments) {
                // 移除当前行的注释：去掉行首的"--"及其前面的空白字符
                val newStr = lineText.replaceFirst("^\\s*--", "")
                document.replaceString(
                  document.getLineStartOffset(curLine),
                  document.getLineEndOffset(curLine),
                  newStr
                )
              } else {
                // 添加注释：在行首插入"-- "
                val insertPos: Int = document.getLineStartOffset(curLine)
                document.insertString(insertPos, "-- ")
              }
            }
          } catch {
            case e: Exception =>
              ExceptionHandle.handleException(e)
          }
        }

      }
    )
  }
}
