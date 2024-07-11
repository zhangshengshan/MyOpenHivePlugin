package action

import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import misc.ExcelObject
import misc.TableExtractUtil.{
  processDorisTables,
  processHiveTables,
  saveDataToExcel
}

class BatchLinageConstrunct extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val editor: Editor = event.getData(CommonDataKeys.EDITOR)

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

      val strings: List[String] =
        list.head.zipWithIndex.map(x => x._1 + ":" + x._2)

      Messages.showInfoMessage(
        strings.mkString("--"),
        "Extracted Tables"
      )

      // 在这里输入一个数值，表示你要处理的表的索引

      // 存储每一个processDorisTables返回的值

      import scala.collection.mutable.ListBuffer

      val mutableList = ListBuffer[(String, String)]()

      import javax.swing.JOptionPane

      val input = JOptionPane.showInputDialog("请输入一个整数")
      val number = input.toInt

      list.tail.foreach(item => {
        //  这里希望能够传递一个统一的路径和一个文件名
        val tuples: List[(String, String)] =
          processDorisTables(item(number), Some(true))
        mutableList ++= tuples
      })
      saveDataToExcel(mutableList.toSet)
    }
  }
}
