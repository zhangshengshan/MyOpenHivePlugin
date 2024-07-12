package action.linage

import action.linage.PlotUtil.plotAction
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.{FileChooserDescriptor, FileChooserDialog, FileChooserFactory}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import config.os.OsConfig
import misc.ExcelObject
import misc.TableExtractUtil.{processDorisTables, saveDataToExcel}
import mydata.studio.MyDataStudio

import scala.collection.mutable.ListBuffer

class PlotLiangeByTableName extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val editor = event.getData(CommonDataKeys.EDITOR)

    // 在这里选择一个Excel文件

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

      plotAction(editor, meta)

    }
  }
}
