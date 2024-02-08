package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, DefaultActionGroup}
import com.intellij.openapi.ui.Messages
import misc.ClipBoardUtil

class BatchProcessGroup extends DefaultActionGroup {

  override def isPopup: Boolean = true
  override def getChildren(e: AnActionEvent): Array[AnAction] = {
    Array(
      new SingleQuoteWrapper,
      new DoubleQuoteWrapper,
      new CommentProcess
    )
  }
}

class SingleQuoteWrapper extends AnAction("单引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val str = clipboard
      .split("\n")
      .map(x => "'" + x.strip() + "'")
      .mkString("(", ",", ")")
    Messages.showInfoMessage(str, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(str)
  }
}

class DoubleQuoteWrapper extends AnAction("双引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val str = clipboard
      .split("\n")
      .map(x => "\"" + x.strip() + "\"")
      .mkString("(", ",", ")")
    Messages.showInfoMessage(str, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(str)
  }
}
