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
      new SaveDorisMetaToXlsx
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

      import java.util.Base64
      val encoded =
        Base64.getEncoder.encodeToString((user + ":" + password).getBytes)
      val headers = Map("Authorization" -> s"Basic $encoded")

      val url = s"http://$host:$port/api/$yourdb/$yourtb/_schema"
      val response = requests.get(url, headers = headers)
      val jsonValue = ujson.read(response.text())

      if (jsonValue("code").num != 0) {
        Messages.showInfoMessage(jsonValue("msg").str, "Error")
        return
      }
      implicit val propertyRW = upickle.default.macroRW[Property]
      implicit val dataRW = upickle.default.macroRW[Data]
      implicit val responseRW = upickle.default.macroRW[Response]
      println(jsonValue)
      val responseObj: Response = upickle.default.read[Response](jsonValue)
      responseObj.data.properties.foreach(item => {
        println(item.name)
      })

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
    val clipboard = ClipBoardUtil.getFromClipboard
    val value: MyConfigurable = MyConfigurable.getInstance()
    val host = value.getHost
    val port = value.getPort
    val user = value.getUser
    val password = value.getPassword
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)

    val selectText = editor.getSelectionModel.getSelectedText
    val model = editor.getCaretModel
    val offset = model.getOffset
    val project: Project = editor.getProject

    try{
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
