package action.util

import com.intellij.openapi.ui.Messages

object ExceptionHandle {
  // 定义一个处理异常的辅助函数
  def handleException(e: Throwable): Unit = {
    // println("发生了一个异常：" + e.getMessage)
    // 其他处理逻辑...
    Messages.showErrorDialog(
      "发生了一个异常：" + e.getMessage,
      "异常"
    )
  }
}
