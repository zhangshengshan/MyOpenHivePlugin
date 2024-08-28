package action.hilight

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.editor.actionSystem.EditorTextInsertHandler
import com.intellij.openapi.editor.actionSystem.TypedAction
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.{PsiDocumentManager, PsiElement, PsiFile, PsiWhiteSpace}
import com.intellij.psi.util.PsiTreeUtil

class HighlightWordAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getProject
    if (project == null) return

    val editor = e.getDataContext.getData("editor")
    if (editor == null || !editor.isInstanceOf[Editor]) return

    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.asInstanceOf[Editor].getDocument)
    if (psiFile == null) return

    val offset = editor.asInstanceOf[Editor].getCaretModel.getOffset
    val elementAt = psiFile.findElementAt(offset)

    if (elementAt == null || elementAt.isInstanceOf[PsiWhiteSpace]) return

    val word = elementAt.getText

    if (word != null && word.nonEmpty) {
      val service = project.getService(classOf[HighlightWordService])
      service.highlightWordInAllOpenFiles(project, word)
    }
  }
}