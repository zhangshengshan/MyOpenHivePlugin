package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.ui.Messages
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import psi.node.IdentifierNode

class HiveWholeProjectRenameAction extends AnAction {
  val SEP = "\r"

  override def actionPerformed(event: AnActionEvent): Unit = {
    var str = ""
    val element: PsiElement = event.getData(CommonDataKeys.PSI_ELEMENT)

    val root: PsiElement =
      PsiTreeUtil.getTopmostParentOfType(element, classOf[PsiElement])

    // TODO: 需要关注一下这里的单位是啥？
    val elements: Array[PsiElement] = PsiTreeUtil.collectElements(
      root,
      {
        case e@(_: IdentifierNode) if e.getText == element.getText =>
          str += e.getText + SEP; true
        case _ => false
      }
    )

    str += "size" + elements.length + SEP
    Messages.showMessageDialog(
      str,
      "HiveWholeProjectRenameAction",
      Messages.getErrorIcon
    )

  }
}
