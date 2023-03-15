package misc

object ClipBoardUtil {
  def copyToClipBoard(str: String) = {
    import java.awt.Toolkit
    import java.awt.datatransfer.{Clipboard, StringSelection}
    val selection = new StringSelection(str);
    val clipboard: Clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
    clipboard.setContents(selection, null);
  }
}
