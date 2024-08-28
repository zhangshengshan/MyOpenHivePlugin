package action.hilight

import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.{
  HighlighterLayer,
  HighlighterTargetArea,
  TextAttributes
}
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.search.{
  GlobalSearchScope,
  PsiSearchHelper,
  TextOccurenceProcessor
}
import com.intellij.psi.{PsiDocumentManager, PsiElement}

import java.awt.{Color, Font}
import scala.collection.mutable.ListBuffer

@Service
final class HighlightWordService {
  private val HIGHLIGHT_KEY =
    TextAttributesKey.createTextAttributesKey(
      "HIGHLIGHT_KEY",
      new TextAttributes(Color.RED, Color.GREEN, null, null, Font.BOLD)
  )

  def highlightWordInAllOpenFiles(project: Project, word: String): Unit = {
    val openFiles = OpenFilesUtil.getAllOpenFiles(project)
    for (file <- openFiles) {
      val editors = OpenFilesUtil.getAllEditors
      for (editor <- editors) {
        if (
          FileDocumentManager.getInstance().getFile(editor.getDocument) == file
        ) highlightWordInEditor(project, editor, word)
      }
    }
  }

  private def highlightWordInEditor(
      project: Project,
      editor: Editor,
      word: String
  ): Unit = {
    val psiFile =
      PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument)
    if (psiFile == null) return

    val ranges = ListBuffer[TextRange]()
    val searchScope = GlobalSearchScope.fileScope(psiFile)
    val searchHelper = PsiSearchHelper.getInstance(project)

    import com.intellij.psi.search.UsageSearchContext
    searchHelper.processElementsWithWord(
      new TextOccurenceProcessor {
        override def execute(
            element: PsiElement,
            offsetInElement: Int
        ): Boolean = {
          val range = element.getTextRange
          ranges += new TextRange(
            range.getStartOffset + offsetInElement,
            range.getStartOffset + offsetInElement + word.length
          )
          true
        }
      },
      searchScope,
      word,
      UsageSearchContext.IN_CODE, // search context
      true, // search in comments
      true // search in string literals
    )

    val markupModel = editor.getMarkupModel
    for (range <- ranges) {
      markupModel.addRangeHighlighter(
        range.getStartOffset,
        range.getEndOffset,
        HighlighterLayer.SELECTION,
        HIGHLIGHT_KEY.getDefaultAttributes,
        HighlighterTargetArea.EXACT_RANGE
      )
    }
  }

  def clearHighlightsInAllOpenFiles(project: Project): Unit = {
    val openFiles = OpenFilesUtil.getAllOpenFiles(project)
    for (file <- openFiles) {
      val editors = OpenFilesUtil.getAllEditors
      for (editor <- editors) {
        if (
          FileDocumentManager.getInstance().getFile(editor.getDocument) == file
        ) {
          clearHighlightsInEditor(editor)
        }
      }
    }
  }

  private def clearHighlightsInEditor(editor: Editor): Unit = {
    val markupModel = editor.getMarkupModel
    markupModel.removeAllHighlighters()
  }

}
