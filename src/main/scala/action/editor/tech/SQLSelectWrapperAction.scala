package action.editor.tech

import action.util.ExceptionHandle
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.{Document, Editor, SelectionModel}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class SQLSelectWrapperAction extends AnAction {

  override def actionPerformed(e: AnActionEvent): Unit = {
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    if (editor == null || project == null) return

    val selectionModel: SelectionModel = editor.getSelectionModel
    val document: Document = editor.getDocument

    val start: Int = selectionModel.getSelectionStart
    val end: Int = selectionModel.getSelectionEnd
    val selectedText: String = selectionModel.getSelectedText
    if (selectedText == null || selectedText.isEmpty) return

    val alias = Messages.showInputDialog(
      project,
      "请输入子查询别名:",
      "SQL Select Wrapper",
      Messages.getQuestionIcon(),
      "t",
      null
    )
    if (alias == null || alias.trim.isEmpty) return

    val newText = s"SELECT * FROM (\n$selectedText\n) AS ${alias.trim}"

    WriteCommandAction.runWriteCommandAction(project, new Runnable {
      override def run(): Unit = {
        try {
          document.replaceString(start, end, newText)
          editor.getCaretModel.moveToOffset(start + newText.length)
        } catch {
          case ex: Throwable =>
            ExceptionHandle.handleException(ex)
        }
      }
    })
  }
}