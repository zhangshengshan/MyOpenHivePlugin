package action.customui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import javax.swing._
import java.util



class MultiChoiceDialog(options: util.List[String]) extends DialogWrapper(true) {
  private var list: JBList[String] = new JBList[String](options)
  list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

  init()
  setTitle("Multi Choice Dialog")

  override protected def createCenterPanel: JComponent = {
    new JScrollPane(list)
  }

  def getSelectedOptions: util.List[String] = list.getSelectedValuesList
}