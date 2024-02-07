package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.ui.DialogWrapper

import javax.swing.JComponent

class ShowCustomDialogAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val dialog = new CustomDialogWrapper
    dialog.showAndGet()
  }
}

class CustomDialogWrapper extends DialogWrapper(true) {
  init()
  setTitle("自定义对话框")

  override def createCenterPanel(): JComponent = {
    CustomDialogExample.showDialog()
  }
}