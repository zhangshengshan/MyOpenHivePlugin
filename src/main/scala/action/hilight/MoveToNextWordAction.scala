package action.hilight

import action.hilight.CaretUtil.getWordAtCaret
import action.hilight.ColorScheme.{BACKGROUD_COLORS, FONT_COLORS}
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.markup.{
  EffectType,
  HighlighterLayer,
  HighlighterTargetArea,
  TextAttributes
}

import scala.util.matching.Regex

class MoveToNextWordAction extends AnAction {

  var colorIndex = 0
  var currentWord: String = ""

  // 当动作触发时的处理函数
  override def actionPerformed(e: AnActionEvent): Unit = {

    // 获取当前编辑器和项目实例
    val editor = e.getData(CommonDataKeys.EDITOR)
    val project = e.getProject
    // 如果编辑器或项目实例为空，则直接返回
    if (editor == null || project == null) {
      return
    }

    // 获取光标模型和当前光标位置
    val caretModel = editor.getCaretModel
    var offset = caretModel.getOffset
    // 获取文档文本
    val documentText = editor.getDocument.getText

    // 获取光标下的单词
    val word = getWordAtCaret(editor)
    // 如果获取不到单词，则直接返回
    if (word == null) {
      return
    }
    // 如果当前的单词和上一次的单词不同，就切换颜色
    if (
      currentWord == null || word.toLowerCase() != currentWord.toLowerCase()
    ) {
      currentWord = word
      colorIndex = (colorIndex + 1) % FONT_COLORS.length
    }

    // 高亮当前的单词
    val markupModel = editor.getMarkupModel

    // 根据当前的颜色索引创建文本属性
    val textAttributes =
      new TextAttributes(
        FONT_COLORS(colorIndex),
        BACKGROUD_COLORS(colorIndex),
        null,
        EffectType.BOXED,
        0
      )

    // 添加高亮标记到文档中
    markupModel.addRangeHighlighter(
      caretModel.getOffset,
      caretModel.getOffset + word.length,
      HighlighterLayer.LAST,
      textAttributes,
      HighlighterTargetArea.EXACT_RANGE
    )

    // 构建单词的正则表达式，用于查找下一个匹配的单词
    val wordPattern = new Regex(s"(?i)\\b$word\\b").unanchored
    // 尝试从当前光标位置之后查找下一个匹配的单词
    var nextWordMatch =
      wordPattern.findFirstMatchIn(documentText.substring(offset + 1))

    // 如果在当前光标位置之后找不到匹配的单词，则从文档的开头重新开始查找
    if (nextWordMatch.isEmpty) {
      offset = 0
      nextWordMatch = wordPattern.findFirstMatchIn(documentText)
    }

    // 如果找到匹配的单词，则滚动到该单词的位置，但不进行高亮
    nextWordMatch.foreach { m =>
      // 如果搜索不是从文档的开头开始，则调整搜索开始的偏移量
      val searchStartOffset = if (offset > 0) 1 else 0
      // 计算下一个匹配单词的偏移量
      val nextWordOffset = offset + m.start + searchStartOffset
      // 将光标移动到下一个匹配的单词位置
      caretModel.moveToOffset(nextWordOffset)
      // 滚动编辑器使当前光标位置居中显示
      editor.getScrollingModel.scrollToCaret(ScrollType.CENTER)
    }

  }
}
