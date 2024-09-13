package action.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange

/**
 * EditorUtil对象提供与编辑器相关的实用程序方法
 */
object EditorUtil {
  /**
   * 获取光标下的文本内容
   *
   * @param editor 编辑器实例，用于操作文本和光标位置
   * @return 返回光标下的文本内容
   */
  def getCaretUnderCursor(editor: Editor): String = {
    // 获取编辑器的光标模型
    val model = editor.getCaretModel
    // 获取光标位置的偏移量
    val offset = model.getOffset
    // 获取当前项目实例
    val project: Project = editor.getProject

    // 初始化左侧偏移量
    var leftOffset = offset
    // 初始化右侧偏移量
    var rightOffset = offset

    // 检查光标前一个字符是否为空白字符（空格、制表符等）
    Character.isWhitespace(
      editor.getDocument.getText.charAt(offset - 1)
    )

    // 向左移动偏移量，直到遇到空白字符或到达文本开头
    while (
      leftOffset > 0 && !Character.isWhitespace(
        editor.getDocument.getText.charAt(leftOffset - 1)
      )
    ) {
      leftOffset = leftOffset - 1
    }
    // 向右移动偏移量，直到遇到空白字符或到达文本末尾
    while (
      rightOffset < editor.getDocument.getTextLength && !Character.isWhitespace(
        editor.getDocument.getText.charAt(rightOffset)
      )
    ) {
      rightOffset = rightOffset + 1
    }

    // 获取左偏移量和右偏移量之间的文本内容
    val expandObj: String = editor.getDocument.getText(
      new TextRange(leftOffset, rightOffset)
    )
    expandObj
  }
}
