package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange

class CommentSelectLinesToggle extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {

    // 获取编辑器选择的行，然后在行首添加 -- 进行注释
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val starts: Array[Int] = editor.getSelectionModel.getBlockSelectionStarts

    val selectionModel = editor.getSelectionModel

    // Get the start and end positions of the selected text
    val start = selectionModel.getSelectionStart
    val end = selectionModel.getSelectionEnd
    val document: Document = editor.getDocument

    val lineStart: Int = document.getLineNumber(start)
    val lineEnd: Int = document.getLineNumber(end)

    WriteCommandAction.runWriteCommandAction(
      project,
      new Runnable {
        override def run(): Unit = {
          try {
            lineStart to lineEnd foreach { curLine =>
              {
                val insertPos: Int = document.getLineStartOffset(curLine)
                // 如果该行的行首没有 -- 则在行首添加 --
                // 如果该行的行首有 -- 则去掉行首的 --

                if (
                  document
                    .getText(new TextRange(insertPos, insertPos + 3)) == "-- "
                ) {
                  document.deleteString(insertPos, insertPos + 3)
                } else {
                  document.insertString(insertPos, "-- ")
                }

              }
            }
          } catch {
            case e => {
              e.printStackTrace()
              // 弹出错误对话框显示错误信息
              Messages.showMessageDialog(
                e.getMessage,
                "Error",
                Messages.getErrorIcon
              )
            }
          }
        }
      }
    )
  }
}
