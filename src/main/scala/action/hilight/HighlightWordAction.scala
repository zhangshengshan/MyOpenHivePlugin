package action.hilight

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.editor.Editor
import com.intellij.psi.{PsiDocumentManager, PsiWhiteSpace}

class HighlightWordAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getProject
    if (project == null) return

    val editor = e.getDataContext.getData("editor")
    if (editor == null || !editor.isInstanceOf[Editor]) return

    val psiFile = PsiDocumentManager
      .getInstance(project)
      .getPsiFile(editor.asInstanceOf[Editor].getDocument)
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

class ClearAllHighlightAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getProject
    if (project == null) return

    val service = project.getService(classOf[HighlightWordService])
    service.clearHighlightsInAllOpenFiles(project)
  }
}
