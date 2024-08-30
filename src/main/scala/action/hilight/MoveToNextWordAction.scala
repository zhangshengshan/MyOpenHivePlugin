package action.hilight

import action.hilight.ColorScheme.{backgroundColors, fontColors}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.markup.{EffectType, HighlighterLayer, HighlighterTargetArea, TextAttributes}
import com.intellij.openapi.editor.{Editor, ScrollType}
import com.intellij.openapi.util.TextRange

import scala.util.matching.Regex

class MoveToNextWordAction extends AnAction {

  var colorIndex = 0
  var currentWord: String = ""

  override def actionPerformed(e: AnActionEvent): Unit = {

    val editor = e.getData(CommonDataKeys.EDITOR)
    val project = e.getProject
    if (editor == null || project == null) {
      return
    }

    val caretModel = editor.getCaretModel
    var offset = caretModel.getOffset
    val documentText = editor.getDocument.getText

    val word = getWordAtCaret(editor)
    if (word == null) {
      return
    }

    // Check if the word has changed
    if (!word.equals(currentWord)) {
      colorIndex = (colorIndex + 1) % fontColors.length
      currentWord = word
    }

    val wordPattern = new Regex(s"\\b$word\\b")
    var nextWordMatch =
      wordPattern.findFirstMatchIn(documentText.substring(offset + 1))

    // If no match is found, start from the beginning of the document
    if (nextWordMatch.isEmpty) {
      offset = 0
      nextWordMatch = wordPattern.findFirstMatchIn(documentText)
    }

    nextWordMatch.foreach { m =>
      // Add 1 to the offset if the search started not at the beginning of the document
      val searchStartOffset = if (offset > 0) 1 else 0
      val nextWordOffset = offset + m.start + searchStartOffset
      caretModel.moveToOffset(nextWordOffset)
      editor.getScrollingModel.scrollToCaret(ScrollType.CENTER)

      // Highlight the new word
      val markupModel = editor.getMarkupModel
      val textAttributes =
        new TextAttributes(
          fontColors(colorIndex),
          backgroundColors(colorIndex),
          null,
          EffectType.BOXED,
          0
        )
      markupModel.addRangeHighlighter(
        nextWordOffset,
        nextWordOffset + word.length,
        HighlighterLayer.LAST,
        textAttributes,
        HighlighterTargetArea.EXACT_RANGE
      )
    }
  }

  /**
   * 在给定的编辑器对象中获取当前光标位置的单词
   * 此方法用于识别编程或文本编辑器中光标所在位置的完整单词
   * 它根据Java标识符的规则来确定单词的边界
   *
   * @param editor 当前的编辑器对象，提供了对光标位置、文档结构的访问
   * @return 光标位置的单词字符串如果光标位于单词内，则返回该单词；如果光标位于单词边界，则返回空字符串
   */
  private def getWordAtCaret(editor: Editor): String = {
    // 获取当前光标的位置在文档中的偏移量
    val caretOffset = editor.getCaretModel.getOffset
    // 获取当前光标所在行的开始偏移量
    val lineStartOffset = editor.getDocument.getLineStartOffset(
      // 获取光标位置所在行的行号
      editor.getDocument.getLineNumber(caretOffset)
    )
    // 获取当前光标所在行的结束偏移量
    val lineEndOffset = editor.getDocument.getLineEndOffset(
      // 再次获取光标位置所在行的行号
      editor.getDocument.getLineNumber(caretOffset)
    )

    // 获取当前行的文本内容
    val lineText =
      editor.getDocument.getText(new TextRange(lineStartOffset, lineEndOffset))
    // 计算光标在当前行中的位置
    val caretPositionInLine = caretOffset - lineStartOffset

    // 从光标位置向左寻找单词的起始边界
    var left = caretPositionInLine
    while (
      // 确保不越界且当前位置字符是Java标识符的一部分
      left > 0 && Character.isJavaIdentifierPart(lineText.charAt(left - 1))
    ) {
      left -= 1
    }

    // 从光标位置向右寻找单词的结束边界
    var right = caretPositionInLine
    while (
      // 确保不越界且当前位置字符是Java标识符的一部分
      right < lineText.length && Character.isJavaIdentifierPart(
        lineText.charAt(right)
      )
    ) {
      right += 1
    }

    // 提取从左边界到右边界的子字符串，即光标位置的单词
    lineText.substring(left, right)
  }
}
