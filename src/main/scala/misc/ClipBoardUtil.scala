package misc

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.{EditorFactoryEvent, EditorFactoryListener, EditorMouseAdapter, EditorMouseEvent}
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.ui.MessageDialogBuilder.Message
import com.intellij.openapi.ui.Messages

import java.awt.Toolkit
import java.awt.datatransfer._
import scala.collection.mutable.Queue

object ClipBoardUtil {
  private val clipboardHistory: Queue[String] = Queue.empty[String]
  private val clipboard: Clipboard = Toolkit.getDefaultToolkit.getSystemClipboard

  private val copyPasteManager = CopyPasteManager.getInstance()

  copyPasteManager.addContentChangedListener(new CopyPasteManager.ContentChangedListener {
    override def contentChanged(oldTransferable: Transferable, newTransferable: Transferable): Unit = {
      if (newTransferable != null && newTransferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        val data = newTransferable.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]
        clipboardHistory.prepend(data)

        // Limit the history size to 100
        if (clipboardHistory.size > 100) {
          clipboardHistory.remove(100)
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