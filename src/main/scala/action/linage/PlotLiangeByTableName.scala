package action.linage

import action.linage.PlotUtil.plotAction
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.ui.Messages
import misc.ExcelObject

import scala.collection.mutable.ListBuffer

class PlotLiangeByTableName extends AnAction {

  // 在这里设置一个字段存储的MetaData, 作为后续的分析的缓存
  val metaData: ListBuffer[(String, String)] = ListBuffer()

  override def actionPerformed(event: AnActionEvent): Unit = {
    val editor = event.getData(CommonDataKeys.EDITOR)

    // 在这里选择一个Excel文件
    Messages.showYesNoDialog(
      "使用上次的缓存吗?",
      "绘画单表血缘",
      "使用上次的缓存",
      "重新读入元数据文件",
      Messages.getQuestionIcon
    ) match {
      case Messages.YES =>
        if (metaData.isEmpty) {
          Messages.showInfoMessage("No Data Found!", "Information")
        } else {
          plotAction(editor, metaData.toList)
        }
      case _ =>
        import javax.swing.JFileChooser
        import javax.swing.filechooser.FileNameExtensionFilter

        val fileChooser = new JFileChooser()
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
        fileChooser.setFileFilter(
          new FileNameExtensionFilter("Excel Files", "xls", "xlsx")
        )

        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
          val file = fileChooser.getSelectedFile
          val filePath = file.getAbsolutePath
          // filePath是用户选择的Excel文件的完整路径
          // 你可以在这里处理这个文件
          val excel = new ExcelObject
          val list: List[List[String]] = excel.readExcel(filePath)
          val meta = list.tail.map(item => (item(0), item(1)))
          if (meta.isEmpty) {
            Messages.showInfoMessage("No Data Found!", "Information")
          } else {
            metaData.clear()
            metaData.appendAll(meta)
          }

          plotAction(editor, metaData.toList)

        }
    }
  }
}
