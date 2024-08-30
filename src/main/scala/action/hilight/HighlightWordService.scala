package action.hilight

import action.hilight.ColorScheme.{backgroundColors, fontColors}
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

  /**
   * 在所有打开的文件中清除高亮标记
   *
   * 此函数的目的是遍历所有打开的文件和编辑器，清除每个文件中的高亮标记
   * 它首先获取所有打开的文件，然后获取所有编辑器，通过比较文件和编辑器中的文件实例，
   * 确保正确的文件对应的编辑器被找到，然后调用私有函数 clearHighlightsInEditor 来清除高亮标记
   *
   * @param project 当前项目，用于获取打开的文件和编辑器
   */
  def clearHighlightsInAllOpenFiles(project: Project): Unit = {
    // 获取当前项目中所有打开的文件
    val openFiles = OpenFilesUtil.getAllOpenFiles(project)
    // 遍历所有打开的文件
    for (file <- openFiles) {
      // 获取所有编辑器实例
      val editors = OpenFilesUtil.getAllEditors
      // 遍历所有编辑器
      for (editor <- editors) {
        // 检查当前编辑器中的文件是否与当前遍历的文件相同
        if (
          FileDocumentManager.getInstance().getFile(editor.getDocument) == file
        ) {
          // 清除当前编辑器中的高亮标记
          clearHighlightsInEditor(editor)
        }
      }
    }
  }

  /**
   * 在指定的编辑器中清除所有高亮标记
   *
   * 此函数通过获取编辑器的标记模型，然后调用 removeAllHighlighters 方法来清除所有高亮标记
   * 它是一个辅助函数，用于 clearHighlightsInAllOpenFiles 函数中清除每个编辑器中的高亮标记
   *
   * @param editor 需要清除高亮标记的编辑器
   */
  private def clearHighlightsInEditor(editor: Editor): Unit = {
    // 获取编辑器的标记模型
    val markupModel = editor.getMarkupModel
    // 清除标记模型中的所有高亮标记
    markupModel.removeAllHighlighters()
  }

}
