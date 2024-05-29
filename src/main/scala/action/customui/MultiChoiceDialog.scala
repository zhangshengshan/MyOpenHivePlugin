package action.customui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import javax.swing._
import java.util



class MultiChoiceDialog(options: util.List[String]) extends DialogWrapper(true) {
  // 在这个组件的上面添加一个Search栏，快速把相关选项筛选出来
  private var list: JBList[String] = new JBList[String](options)
  list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

  init()
  setTitle("Multi Choice Dialog")

  override protected def createCenterPanel: JComponent = {
    new JScrollPane(list)
  }

  def getSelectedOptions: util.List[String] = list.getSelectedValuesList
}