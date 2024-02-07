package action

import com.intellij.openapi.actionSystem.{ActionGroup, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.messages.MessageDialog

class MyActionGroup extends DefaultActionGroup {

  override def isPopup: Boolean = true
  override def getChildren(e: AnActionEvent): Array[AnAction] = {
    Array(
      new MySubAction1,
      new MySubAction2
    )
  }
}

class MySubAction1 extends AnAction("子命令1") {
  override def actionPerformed(e: AnActionEvent): Unit = {

    Messages.showMessageDialog("子命令1", "Information", Messages.getInformationIcon)

    // 在这里实现子命令1的功能
  }
}

class MySubAction2 extends AnAction("子命令2"){
  override def actionPerformed(e: AnActionEvent): Unit = {

    Messages.showMessageDialog("子命令2!", "Information", Messages.getInformationIcon)
    // 在这里实现子命令2的功能
  }
}
