package misc

import com.intellij.openapi.ide.CopyPasteManager

import java.awt.datatransfer.{DataFlavor, Transferable}

object ClipBoardUtil {
  def copyToClipBoard(str: String): Unit = {
    import java.awt.Toolkit
    import java.awt.datatransfer.{Clipboard, StringSelection}
    val selection = new StringSelection(str)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
    clipboard.setContents(selection, null)
  }

  def getFromClipboard: String = {
    val contents = CopyPasteManager.getInstance.getContents
    if (contents != null) try if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) return contents.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    null
  }
}
