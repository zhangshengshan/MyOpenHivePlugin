package action.hilight

import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.{HighlighterLayer, HighlighterTargetArea, TextAttributes}
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.search.{GlobalSearchScope, PsiSearchHelper, TextOccurenceProcessor}
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

  // 定义一个私有的颜色数组，用于存储字体颜色
  private val fontColors = Array(
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.ORANGE,
    Color.PINK
  )

  // 定义一个私有的颜色数组，用于存储背景颜色，其中的颜色是根据字体颜色的补色原则选择的
  private val backgroundColors = Array(
    Color.CYAN, // Complement of RED
    Color.MAGENTA, // Complement of GREEN
    Color.YELLOW, // Complement of BLUE
    Color.BLUE, // Complement of YELLOW
    Color.RED, // Complement of CYAN
    Color.GREEN, // Complement of MAGENTA
    Color.BLUE, // Complement of ORANGE
    Color.GREEN // Complement of PINK
  )


  private var colorIndex = 0

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

    colorIndex = (colorIndex + 1) % fontColors.length
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

//    val markupModel = editor.getMarkupModel
//    for (range <- ranges) {
//      markupModel.addRangeHighlighter(
//        range.getStartOffset,
//        range.getEndOffset,
//        HighlighterLayer.SELECTION,
//        HIGHLIGHT_KEY.getDefaultAttributes,
//        HighlighterTargetArea.EXACT_RANGE
//      )
//    }

    val markupModel = editor.getMarkupModel
    for (range <- ranges) {
      val highlightKey = TextAttributesKey.createTextAttributesKey(
        "HIGHLIGHT_KEY",
        new TextAttributes(
          fontColors(colorIndex),
          backgroundColors(colorIndex),
          null,
          null,
          Font.BOLD
        )
      )
      markupModel.addRangeHighlighter(
        range.getStartOffset,
        range.getEndOffset,
        HighlighterLayer.SELECTION,
        highlightKey.getDefaultAttributes,
        HighlighterTargetArea.EXACT_RANGE
      )
    }

    // Update the color index for the next highlight
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
