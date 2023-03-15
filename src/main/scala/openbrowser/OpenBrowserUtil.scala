package openbrowser

import java.awt.Desktop
import java.net.URI

class OpenBrowserUtil

object OpenBrowserUtil {
  def open(url: String) = {
    if (Desktop.isDesktopSupported) {
      Desktop.getDesktop.browse(new URI(url))
    }
  }
}
