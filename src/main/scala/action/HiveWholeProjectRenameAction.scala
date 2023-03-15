package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFile, PsiReference}
import psi.node.IdentifierNode

class HiveWholeProjectRenameAction extends AnAction {
  val SEP = "\r"

  override def actionPerformed(event: AnActionEvent): Unit = {
    var str = ""

    val project: Project = event.getProject

    val file: PsiFile = event.getData(CommonDataKeys.PSI_FILE)
    val element: PsiElement = event.getData(CommonDataKeys.PSI_ELEMENT)

    val reference: PsiReference = element.getReference
    println(reference.getCanonicalText)

    val root: PsiElement =
      PsiTreeUtil.getTopmostParentOfType(element, classOf[PsiElement])

    // TODO: 需要关注一下这里的单位是啥？
    val elements: Array[PsiElement] = PsiTreeUtil.collectElements(
      root,
      (e: PsiElement) => {
        e match {
          case _: IdentifierNode if e.getText == element.getText =>
            str += e.getText + SEP; true
          case _ => false
        }
      }
    )

    //    // TODO: 这里是整个工程替换，考虑整一个当前文件的修改
    //    val refactoringFactory = new RefactoringFactoryImpl(project)
    //
    //    val inputStr: String = Messages.showInputDialog(
    //      project,
    //      "Replace all " + element.getText + " with:",
    //      "Replace",
    //      Messages.getQuestionIcon,
    //      "",
    //      null
    //    )
    //    // 这里的
    //    elements.foreach(e => {
    //      val renameRefactoring = refactoringFactory.createRename(e, inputStr)
    //      renameRefactoring.run()
    //    })

    str += "size" + elements.size + SEP
    Messages.showMessageDialog(
      str,
      "HiveWholeProjectRenameAction",
      Messages.getErrorIcon
    )

  }
}
