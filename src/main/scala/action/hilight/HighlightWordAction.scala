package action.hilight

import action.hilight.HighlightWordService
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

import javax.swing.JOptionPane

class HighlightWordAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getProject
    if (project == null) return

    val word = JOptionPane.showInputDialog("Enter word to highlight:")
    if (word != null && word.nonEmpty) {
      val service =
        project.getService(classOf[HighlightWordService])
      service.highlightWordInAllOpenFiles(project, word)
    }
  }
}
