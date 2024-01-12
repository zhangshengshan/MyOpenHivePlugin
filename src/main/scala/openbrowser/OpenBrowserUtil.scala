package openbrowser

import java.awt.Desktop
import java.net.{URI, URISyntaxException}

class OpenBrowserUtil

object OpenBrowserUtil {
  /**
   * 打开网页
   * 如果系统不支持使用Desktop应用，则不执行任何操作。
   * 如果传入的URL是非法的，会抛出异常。
   *
   * @param url 要打开的网页链接
   */
  def open(url: String): Unit = {
    if (Desktop.isDesktopSupported && url != null && url.trim.nonEmpty) {
      try {
        Desktop.getDesktop.browse(new URI(url))
      } catch {
        case e: URISyntaxException =>
          println(s"Invalid URL: $url")
          // 处理URISyntaxException，例如记录日志或通知用户
      }
    }
  }
}
