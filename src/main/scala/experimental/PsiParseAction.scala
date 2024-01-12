package experimental

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.editor._
import com.intellij.openapi.fileEditor.{FileDocumentManager, FileEditorManager}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiManager}
import psi.node.IdentifierNode

class PsiParseAction extends AnAction {

  override def actionPerformed(event: AnActionEvent): Unit = {
    val project: Project = event.getProject
    val editor: Editor =
      FileEditorManager.getInstance(project).getSelectedTextEditor
    val file: VirtualFile =
      FileDocumentManager.getInstance.getFile(editor.getDocument)
    val offset: Int = editor.getCaretModel.getOffset

    val element: PsiElement =
      PsiManager.getInstance(project).findFile(file).findElementAt(offset)

    val text: String = element.getText

    Messages.showInfoMessage(project, text, "PsiParseAction")
    Messages.showInfoMessage(
      project,
      "current offset:" + element.getTextOffset.toString,
      "PsiParseAction"
    )

    val root: PsiElement =
      PsiTreeUtil.getTopmostParentOfType(element, classOf[PsiElement])
    PsiTreeUtil
      .collectElements(
        root,
        {
          case e@(_: IdentifierNode) => e.getText == element.getText
          case _ => false
        }
      )
      .foreach((e: PsiElement) => {
        Messages.showInfoMessage(
          project,
          e.getText + " offset:" + e.getTextOffset,
          "PsiParseAction"
        )
      })

    val ref: PsiElement =
      element.asInstanceOf[IdentifierNode].getReference.resolve()

    val i: Int = PsiTreeUtil.getDepth(root, ref)
    val i1: Int = PsiTreeUtil.getDepth(root, element)

    Messages.showInfoMessage(
      project,
      ref.getText + " 引用坐标:" + ref.getTextOffset +
        "引用深度" + i.toString + " 当前深度" + i1.toString,
      "PsiParseAction"
    )

    val model: CaretModel = editor.getCaretModel
    val caret: Caret = model.getPrimaryCaret
    val position: LogicalPosition = caret.getLogicalPosition
    val position1: VisualPosition = caret.getVisualPosition

    Messages.showInfoMessage(
      project,
      "LogicalPosition:" + position.toString + " VisualPosition:" + position1.toString,
      "PsiParseAction"
    )
  }
}
