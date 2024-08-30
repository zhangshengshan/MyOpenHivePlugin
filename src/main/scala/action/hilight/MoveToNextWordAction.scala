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

  private def getWordAtCaret(editor: Editor): String = {
    val caretOffset = editor.getCaretModel.getOffset
    val lineStartOffset = editor.getDocument.getLineStartOffset(
      editor.getDocument.getLineNumber(caretOffset)
    )
    val lineEndOffset = editor.getDocument.getLineEndOffset(
      editor.getDocument.getLineNumber(caretOffset)
    )

    val lineText =
      editor.getDocument.getText(new TextRange(lineStartOffset, lineEndOffset))
    val caretPositionInLine = caretOffset - lineStartOffset

    var left = caretPositionInLine
    while (
      left > 0 && Character.isJavaIdentifierPart(lineText.charAt(left - 1))
    ) {
      left -= 1
    }

    var right = caretPositionInLine
    while (
      right < lineText.length && Character.isJavaIdentifierPart(
        lineText.charAt(right)
      )
    ) {
      right += 1
    }

    lineText.substring(left, right)
  }
}
