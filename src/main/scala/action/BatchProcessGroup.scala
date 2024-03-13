package action

import another.ClusDbTbNode
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys,
  DefaultActionGroup
}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.{
  FileChooserDescriptor,
  FileChooserDialog,
  FileChooserFactory
}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.zss.graph.{Graph, Node}
import config.os.OsConfig
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil

import java.io.{File, FileOutputStream}

class BatchProcessGroup extends DefaultActionGroup {

  override def isPopup: Boolean = true
  override def getChildren(e: AnActionEvent): Array[AnAction] = {
    Array(
      new SingleQuoteWrapper,
      new DoubleQuoteWrapper,
      new CommentProcess,
      new SaveDorisMetaToXlsx,
      new CompareTwoTables
    )
  }
}

class SingleQuoteWrapper extends AnAction("单引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val str = clipboard
      .split("\n")
      .map(x => "'" + x.strip() + "'")
      .mkString("(", ",", ")")
    Messages.showInfoMessage(str, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(str)
  }
}

class DoubleQuoteWrapper extends AnAction("双引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val str = clipboard
      .split("\n")
      .map(x => "\"" + x.strip() + "\"")
      .mkString("(", ",", ")")
    Messages.showInfoMessage(str, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(str)
  }

}

class CompareTwoTables extends AnAction("表格比对") {

  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {
    val (clipboard, host, port, user, password, project) =
      misc.GetConfig.getConfig(anActionEvent)

    Messages.showInfoMessage(clipboard, "剪切板内容")
    try {
      val sourceTable = clipboard.strip().split("\n")(0)
      val targetTable = clipboard.strip().split("\n")(1)
      val sourceTableMeta = misc.DorisHttpUtil.getTableMeta(
        user,
        password,
        host,
        port,
        sourceTable.split("\\.")(0),
        sourceTable.split("\\.")(1)
      )
      val targetTableMeta = misc.DorisHttpUtil.getTableMeta(
        user,
        password,
        host,
        port,
        targetTable.split("\\.")(0),
        targetTable.split("\\.")(1)
      )
      val sourceTableMetaMap =
        sourceTableMeta.get.data.properties.map(x => (x.name, x.comment)).toMap
      val targetTableMetaMap =
        targetTableMeta.get.data.properties.map(x => (x.name, x.comment)).toMap
      // here we can compare the two maps
      if (sourceTableMetaMap sameElements targetTableMetaMap) {
        //进一步生成SQL来进行比较
        val sstr = sourceTableMeta.get.data.properties
          .map(item =>
            s"SUM(${item.name}) AS SUM_${item.name}, MAX(${item.name}) AS MAX_${item.name} "
          )
          .mkString("SELECT ", ",", s" FROM ${sourceTable};")
        val tstr = targetTableMeta.get.data.properties
          .map(item =>
            s"SUM(${item.name}) AS SUM_${item.name}, MAX(${item.name}) AS MAX_${item.name} "
          )
          .mkString("SELECT ", ",", s" FROM ${targetTable};")
        ClipBoardUtil.copyToClipBoard(sstr + System.lineSeparator + tstr)
      } else {
        Messages.showInfoMessage("两个表格不一致", "两个表格不一致")
      }
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        Messages.showMessageDialog(
          e.getMessage,
          "Error",
          Messages.getErrorIcon
        )

    }
  }
}
class SaveDorisMetaToXlsx extends AnAction("保存元数据") {

  import org.apache.poi.xssf.usermodel.XSSFWorkbook
  import org.apache.poi.ss.usermodel.{Cell, Row, Sheet, Workbook}

  var listRowIdx = 2

  private def getXlsxFileFromDoris(
      responseObj: Response,
      workbook: Workbook,
      db: String,
      tb: String,
      listsheet: Sheet
  ) = {

    val tableName = db + "." + tb

    val sheet: Sheet = workbook.createSheet(tb)

    val row0 = sheet.createRow(0)
    val cell0 = row0.createCell(0)
    cell0.setCellValue(tableName)

    responseObj.data.properties.zipWithIndex.foreach {

      case (item, index) => {
        val row: Row = sheet.createRow(index + 2)

        val cell0: Cell = row.createCell(0)
        cell0.setCellValue(tableName)

        val cell1: Cell = row.createCell(1)
        cell1.setCellValue(item.name)

        val cell2: Cell = row.createCell(2)
        cell2.setCellValue(item.comment)

        val listRow = listsheet.createRow(listRowIdx)
        val listCell0: Cell = listRow.createCell(0)
        listCell0.setCellValue(tableName)
        val listCell1: Cell = listRow.createCell(1)
        listCell1.setCellValue(item.name)
        val listCell2: Cell = listRow.createCell(2)
        listCell2.setCellValue(item.comment)

        listRowIdx += 1
      }
    }
  }
  def processDorisSchema(
      tableList: List[String],
      user: String,
      password: String,
      host: String,
      port: String,
      project: Project
  ): Unit = {

    val descriptor: FileChooserDescriptor =
      new FileChooserDescriptor(false, true, false, false, false, false)

    val dialog: FileChooserDialog = FileChooserFactory
      .getInstance()
      .createFileChooser(descriptor, project, null)

    val option: Option[VirtualFile] = dialog.choose(project).headOption
    val outPutDir: String = {
      if (option.isDefined) option.get.getPath
      else if (SystemInfo.isMac) OsConfig.macOutputPath
      else OsConfig.winOutputPath
    }

    var workbook: Workbook =
      new XSSFWorkbook() // new HSSFWorkbook() for generating `.xls` file

    var listsheet = workbook.createSheet("all")

    val fileName: String = Messages.showInputDialog(
      project,
      "请输入文件名",
      "文件名",
      Messages.getQuestionIcon,
      "HelloWorld",
      null
    )

    tableList.foreach(table => {
      val yourdb = table.split("\\.")(0)
      val yourtb = table.split("\\.")(1)

      Messages.showInfoMessage(
        s"正在处理 $yourdb.$yourtb",
        "正在处理 $yourdb.$yourtb"
      )

      val responseObj: Response = misc.DorisHttpUtil
        .getTableMeta(user, password, host, port, yourdb, yourtb) match {
        case Some(result) => result
        case None         => return
      }

      val fieldComments =
        responseObj.data.properties
          .map(item => (item.name, item.comment))
          .toList
      getXlsxFileFromDoris(responseObj, workbook, yourdb, yourtb, listsheet)
    })

    val fileOut = new FileOutputStream(
      outPutDir + File.separator + fileName + ".xlsx"
    )
    workbook.write(fileOut)
    fileOut.close()
    // Closing the workbook
    workbook.close()
  }

  override def actionPerformed(e: AnActionEvent): Unit = {
    val (
      clipboard: String,
      host: String,
      port: String,
      user: String,
      password: String,
      project: Project
    ) = misc.GetConfig.getConfig(e)

    try {
      processDorisSchema(
        clipboard.strip().split("\n").toList,
        user,
        password,
        host,
        port,
        project
      )
      Messages.showInfoMessage("保存成功", "保存成功")
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        Messages.showMessageDialog(
          e.getMessage,
          "Error",
          Messages.getErrorIcon
        )
    }
  }

}
