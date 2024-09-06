package action.hilight

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.psi.{PsiDocumentManager, PsiWhiteSpace}

class HighlightWordAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    // 获取当前项目
    val project = e.getProject
    // 如果项目为空，则直接返回
    if (project == null) return

    // 从事件的数据上下文中获取编辑器实例
    val editor = e.getDataContext.getData("editor")
    // 如果编辑器为空，或不实例化为Editor类型，则直接返回
    if (editor == null || !editor.isInstanceOf[Editor]) return

    // action.hilight.MoveToNextWordAction#getWordAtCaret
    val moveToNextWordAction = new MoveToNextWordAction
    val word = moveToNextWordAction.getWordAtCaret(editor.asInstanceOf[Editor])

//    Messages.showMessageDialog(
//      project,
//      word,
//      "Word at Caret",
//      Messages.getInformationIcon
//    )

    // 如果文本内容非空且非空字符串，则进行高亮处理
    if (word != null && word.nonEmpty) {
      // 在项目中获取HighlightWordService服务
      val service = project.getService(classOf[HighlightWordService])
      // 对所有打开的文件中与找到的文本内容匹配的单词进行高亮
      service.highlightWordInAllOpenFiles(project, word)
    }
  }
}

/** 定义一个清除所有高亮标记的操作类，继承自AnAction
  * 该类的作用是当用户执行该操作时，清除所有打开文件中的高亮标记
  */
class ClearAllHighlightAction extends AnAction {

  /** 当动作被触发时执行的方法
    * 它从当前项目中获取HighlightWordService服务，并调用其方法来清除所有打开文件中的高亮标记
    *
    * @param e AnActionEvent事件对象，包含与操作相关的所有信息
    */
  override def actionPerformed(e: AnActionEvent): Unit = {
    // 从事件对象中获取当前项目
    val project = e.getProject
    // 如果项目不存在，则直接返回，不执行后续操作
    if (project == null) return

    // 从项目中获取HighlightWordService服务
    val service = project.getService(classOf[HighlightWordService])
    // 调用服务的方法，清除所有打开文件中的高亮标记
    service.clearHighlightsInAllOpenFiles(project)
  }
}
