package misc

import java.awt.Toolkit
import java.awt.datatransfer._
import scala.collection.mutable.Queue

object ClipBoardUtil {
  private val clipboardHistory: Queue[String] = Queue.empty[String]
  private val clipboard: Clipboard = Toolkit.getDefaultToolkit.getSystemClipboard

  clipboard.addFlavorListener(new FlavorListener {
    override def flavorsChanged(e: FlavorEvent): Unit = {
      val contents = clipboard.getContents(null)
      if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        val data = contents.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]
        clipboardHistory.enqueue(data)

        // Limit the history size to 100
        if (clipboardHistory.size > 100) {
          clipboardHistory.dequeue()
        }
      }
    }
  })

  def copyToClipBoard(str: String): Unit = {
    val selection = new StringSelection(str)
    clipboard.setContents(selection, null)
  }

  def getFromClipboard: String = {
    val contents = clipboard.getContents(null)
    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      contents.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]
    } else {
      null
    }
  }

  def getClipboardHistory: List[String] = clipboardHistory.toList
}