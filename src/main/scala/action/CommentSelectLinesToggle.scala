package action

import action.util.ExceptionHandle
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
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
                  if (
                    lineText.contains("varchar") || lineText.contains("text")
                  ) {
                    val newLineText =
                      lineText.replaceAll("varchar\\(.*\\)|text", "string")

                    document.replaceString(
                      document.getLineStartOffset(curLine),
                      document.getLineEndOffset(curLine),
                      newLineText
                    )
                  }

                } else {

                  val pattern = "^\\s*--.*".r

                  /** 如果一行是以 -- 开头的，则去掉 --，否则在行首添加 --
                    * 或者-- 前面有若干空格或者若干tab ，则去掉，否则添加 --
                    */
                  if (
                    pattern
                      .findFirstIn(
                        document.getText(
                          new TextRange(
                            document.getLineStartOffset(curLine),
                            document.getLineEndOffset(curLine)
                          )
                        )
                      )
                      .isDefined
                  ) {
                    val str = document.getText(
                      new TextRange(
                        document.getLineStartOffset(curLine),
                        document.getLineEndOffset(curLine)
                      )
                    )
                    val newStr = str.replaceAll("^\\s*--", "")

                    document.replaceString(
                      document.getLineStartOffset(curLine),
                      document.getLineEndOffset(curLine),
                      newStr
                    )

                  } else {
                    document.insertString(insertPos, "-- ")
                  }

                }

              }
            }
          } catch {
            case e: Exception => ExceptionHandle.handleException(e)
          }
        }
      }
    )
  }
}
