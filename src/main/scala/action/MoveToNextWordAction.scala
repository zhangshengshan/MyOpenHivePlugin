package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.markup.{HighlighterLayer, HighlighterTargetArea, TextAttributes}
import com.intellij.openapi.editor.{Editor, ScrollType}
import com.intellij.openapi.util.TextRange

import java.awt.Color
import scala.util.matching.Regex

class MoveToNextWordAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val editor = e.getData(CommonDataKeys.EDITOR)
    val project = e.getProject
    if (editor == null || project == null) {
      return
    }

    val caretModel = editor.getCaretModel
    val offset = caretModel.getOffset
    val documentText = editor.getDocument.getText

    val word = getWordAtCaret(editor)
    if (word == null) {
      return
    }

    val wordPattern = new Regex(s"\\b$word\\b")
    val nextWordMatch =
      wordPattern.findFirstMatchIn(documentText.substring(offset + 1))

    nextWordMatch.foreach { m =>
      val nextWordOffset = offset + 1 + m.start
      caretModel.moveToOffset(nextWordOffset)
      editor.getScrollingModel.scrollToCaret(ScrollType.CENTER)

      // Highlight the new word
      val markupModel = editor.getMarkupModel
      val textAttributes = new TextAttributes(Color.YELLOW, null, null, null, 0)
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
