package action

import com.intellij.openapi.actionSystem.{ActionGroup, AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.messages.MessageDialog
import misc.ClipBoardUtil

class MyActionGroup extends DefaultActionGroup {

  override def isPopup: Boolean = true
  override def getChildren(e: AnActionEvent): Array[AnAction] = {
    Array(
      new SingleQuoteWrapper,
      new DoubleQuoteWrapper
    )
  }
}

class SingleQuoteWrapper extends AnAction("单引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val str = clipboard.split("\n").map(x => "'" + x.strip() + "'").mkString("(", ",", ")")
    Messages.showInfoMessage(str, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(str)
  }
}

class DoubleQuoteWrapper extends AnAction("双引号"){
  override def actionPerformed(e: AnActionEvent): Unit = {

    val clipboard = ClipBoardUtil.getFromClipboard
    val str = clipboard.split("\n").map(x => "\"" + x.strip() + "'").mkString(",")
    Messages.showInfoMessage(str, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(str)
  }
}
