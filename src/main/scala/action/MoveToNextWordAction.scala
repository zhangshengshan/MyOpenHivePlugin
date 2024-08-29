package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.{CaretModel, Editor, ScrollType}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import org.jetbrains.annotations.NotNull

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

    val nextWordOffset = documentText.indexOf(word, offset + 1)
    if (nextWordOffset != -1) {
      caretModel.moveToOffset(nextWordOffset)
      editor.getScrollingModel.scrollToCaret(ScrollType.CENTER)
    }
  }

  private def getWordAtCaret(editor: Editor): String = {
    val caretOffset = editor.getCaretModel.getOffset
    val lineStartOffset = editor.getDocument.getLineStartOffset(editor.getDocument.getLineNumber(caretOffset))
    val lineEndOffset = editor.getDocument.getLineEndOffset(editor.getDocument.getLineNumber(caretOffset))
    val lineText = editor.getDocument.getText(new TextRange(lineStartOffset, lineEndOffset))
    val caretPositionInLine = caretOffset - lineStartOffset

    var left = caretPositionInLine
    while (left > 0 && Character.isJavaIdentifierPart(lineText.charAt(left - 1))) {
      left -= 1
    }
    var right = caretPositionInLine
    while (right < lineText.length && Character.isJavaIdentifierPart(lineText.charAt(right))) {
      right += 1
    }
    lineText.substring(left, right)
  }
}