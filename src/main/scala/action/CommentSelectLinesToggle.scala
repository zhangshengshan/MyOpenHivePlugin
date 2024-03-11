package action

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

    // 获取编辑器选择的行，然后在行首添加 -- 进行注释
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)

    val selectionModel: SelectionModel = editor.getSelectionModel

    // Get the start and end positions of the selected text
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

    WriteCommandAction.runWriteCommandAction(
      project,
      new Runnable {
        override def run(): Unit = {
          try {
            lineStart to lineEnd foreach { curLine: Int =>
              {
                val insertPos: Int = document.getLineStartOffset(curLine)
                // 如果该行的行首没有 -- 则在行首添加 --
                // 如果该行的行首有 -- 则去掉行首的 --

                if (subMode == "ddl") {
                  // get the text of the current line
                  val lineText: String = document.getText(
                    new TextRange(
                      document.getLineStartOffset(curLine),
                      document.getLineEndOffset(curLine)
                    )
                  )
                  // if the line contains varchar(*) 这种模式，替换为string
                  if (lineText.contains("varchar") || lineText.contains("text")) {
                    val newLineText =
                      lineText.replaceAll("varchar\\(.*\\)|text", "string")

                    document.replaceString(
                      document.getLineStartOffset(curLine),
                      document.getLineEndOffset(curLine),
                      newLineText
                    )
                  }

                } else {
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
            }
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
        }
      }
    )
  }
}
