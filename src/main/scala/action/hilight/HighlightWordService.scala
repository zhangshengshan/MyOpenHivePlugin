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

import java.awt.Font
import scala.collection.mutable.ListBuffer

@Service
final class HighlightWordService {
//  private val HIGHLIGHT_KEY =
//    TextAttributesKey.createTextAttributesKey(
//      "HIGHLIGHT_KEY",
//      new TextAttributes(Color.RED, Color.GREEN, null, null, Font.BOLD)
//    )
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

// 在编辑器中高亮显示指定的单词
// 参数:
// project: Project - IDEA的项目实例
// editor: Editor - 当前编辑器实例
// word: String - 需要高亮显示的单词
  private def highlightWordInEditor(
      project: Project,
      editor: Editor,
      word: String
  ): Unit = {
    // 获取当前编辑器文档对应的PsiFile
    val psiFile =
      PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument)
    // 如果PsiFile为空，则直接返回
    if (psiFile == null) return

    // 存储需要高亮显示的文本范围
    val ranges = ListBuffer[TextRange]()
    // 设置搜索范围为当前文件
    val searchScope = GlobalSearchScope.fileScope(psiFile)
    // 获取Psi搜索帮助实例
    val searchHelper = PsiSearchHelper.getInstance(project)

    // 导入搜索上下文
    import com.intellij.psi.search.UsageSearchContext
    // 使用自定义的TextOccurenceProcessor处理文件中所有指定单词的出现
    searchHelper.processElementsWithWord(
      new TextOccurenceProcessor {
        // 执行处理逻辑，为每个单词计算文本范围并添加到ranges列表中
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
      UsageSearchContext.IN_CODE, // 搜索上下文，表示在代码中搜索
      true, // 是否在注释中搜索
      true // 是否在字符串字面量中搜索
    )

    // 获取编辑器的标记模型
    val markupModel = editor.getMarkupModel
    // 遍历ranges，为每个范围添加高亮显示
    for (range <- ranges) {
      // 定义高亮显示的文本属性键
      val highlightKey = TextAttributesKey.createTextAttributesKey(
        s"HIGHLIGHT_KEY_$colorIndex",
        new TextAttributes(
          fontColors(colorIndex),
          backgroundColors(colorIndex),
          null,
          null,
          Font.BOLD
        )
      )
      // 添加高亮显示，使用定义的属性键和选择层
      markupModel.addRangeHighlighter(
        range.getStartOffset,
        range.getEndOffset,
        HighlighterLayer.SELECTION,
        highlightKey.getDefaultAttributes,
        HighlighterTargetArea.EXACT_RANGE
      )
    }

    // 更新下次高亮显示的颜色索引
  }

  // 注意：部分代码（如fontColors和backgroundColors方法调用）未在给定的代码片段中定义，
  // 可能需要在实际使用中定义这些方法或替换为实际的颜色值。

  /** 在所有打开的文件中清除高亮标记
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

  /** 在指定的编辑器中清除所有高亮标记
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
