package action

import action.extract.DorisTableModifier
import another.ClusDbTbNode
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys, DefaultActionGroup}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.{FileChooserDescriptor, FileChooserDialog, FileChooserFactory}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.zss.graph.{Graph, Node}
import config.os.OsConfig
import doris.{DorisLexer, DorisParser}
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

import java.io.{File, FileOutputStream}

class BatchProcessGroup extends DefaultActionGroup {

  override def isPopup: Boolean = true
  override def getChildren(e: AnActionEvent): Array[AnAction] = {
    Array(
      new SingleQuoteWrapper,
      new DoubleQuoteWrapper,
      new CommentProcess,
      new SaveDorisMetaToXlsx,
      new CompareTwoTables,
      new DDLHelper
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
        sourceTableMeta.get.data.properties.map(x => (x.name, x.`type`)).toMap
      val targetTableMetaMap =
        targetTableMeta.get.data.properties.map(x => (x.name, x.`type`)).toMap

      if (!sourceTableMetaMap.keys.sameElements(targetTableMetaMap.keys)) {
        val inter =
          sourceTableMetaMap.keys.toSeq intersect sourceTableMetaMap.keys.toSeq
        val all =
          sourceTableMetaMap.keys.toSeq union targetTableMetaMap.keys.toSeq
        val diff = all diff inter
        Messages.showInfoMessage(s"不一致的字段有${diff.mkString(",")}", "不一致的字段")

        // 是否要对inter部分进行接下来的比较处理， 如果否则不返回
        val result =
          Messages.showYesNoDialog("是否继续?", "确认", Messages.getWarningIcon)
        if (result == Messages.YES) {
          // 用户点击了 "Yes"
        } else if (result == Messages.NO) {
          // 用户点击了 "No"
          return
        }
      }

      if (!sourceTableMetaMap.sameElements(targetTableMetaMap)) {

        val joinedMap: Map[String, (String, String)] =
          sourceTableMetaMap.keys.flatMap { key =>
            for {
              sourceValue <- sourceTableMetaMap.get(key)
              targetValue <- targetTableMetaMap.get(key)
            } yield key -> (sourceValue, targetValue)
          }.toMap

        val str = joinedMap
          .filter(x => x._2._1 != x._2._2)
          .map(x => s"${x._1} ${x._2._1} not equal ${x._2._2}")
          .mkString("不一致的字段有", System.lineSeparator, "请检查")
        Messages.showWarningDialog(str, "不一致的字段")

        val result =
          Messages.showYesNoDialog("是否继续?", "确认", Messages.getWarningIcon)
        if (result == Messages.YES) {
          // 用户点击了 "Yes"
        } else if (result == Messages.NO) {
          // 用户点击了 "No"
          return
        }
      }
      // here we can compare the two maps
      val bothSids =
        sourceTableMetaMap.keys.toSeq intersect targetTableMetaMap.keys.toSeq

      //进一步生成SQL来进行比较
      val sstr = sourceTableMeta.get.data.properties
        .filter(x => bothSids.contains(x.name))
        .map(item => {
          s"MIN(${item.name}) AS MIN_${item.name}, MAX(${item.name}) AS MAX_${item.name}, COUNT(DISTINCT(${item.name})) AS COUNT_${item.name} "
        })
        .mkString(
          "SELECT \n COUNT(*) AS total_cnt, \n",
          ",\n",
          s" FROM ${sourceTable}"
        )
      val tstr = targetTableMeta.get.data.properties
        .filter(x => bothSids.contains(x.name))
        .map(item =>
          s"MIN(${item.name}) AS MIN_${item.name}, MAX(${item.name}) AS MAX_${item.name}, COUNT(DISTINCT(${item.name})) AS COUNT_${item.name} "
        )
        .mkString(
          "SELECT \n COUNT(*) AS total_cnt, \n",
          ",\n",
          s" FROM ${targetTable}"
        )
      ClipBoardUtil.copyToClipBoard(
        sstr + System.lineSeparator + "UNION ALL" + System
          .lineSeparator() + tstr
      )
      Messages.showInfoMessage("已复制到剪切板", "提示")

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
class DDLHelper extends AnAction("DDL修正") {

  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {

    // obtain the content of current file
    val editor: Editor = anActionEvent.getData(CommonDataKeys.EDITOR)
    val text: String = editor.getDocument.getText

    // process the text with Doris

    val lexer = new DorisLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser: DorisParser = new DorisParser(commonTokenStream)

    val context = parser.multiStatements()

    val tokenStreamRewriter =
      new org.antlr.v4.runtime.TokenStreamRewriter(commonTokenStream)
    val visitor = new DorisTableModifier(tokenStreamRewriter)
    visitor.visit(context)
    val newText = tokenStreamRewriter.getText()
    Messages.showInfoMessage(newText, "修正后的DDL")
    ClipBoardUtil.copyToClipBoard(newText)
    // 是否要用newText去覆盖文件
    val result =
      Messages.showYesNoDialog("是否覆盖当前文件?", "确认", Messages.getWarningIcon)

    if (result == Messages.YES) {
      // 用户点击了 "Yes"
      ApplicationManager.getApplication.runWriteAction(new Runnable {
        override def run(): Unit = {
          editor.getDocument.setText(newText)
        }
      })
    } else if (result == Messages.NO) {
      // 用户点击了 "No"
      return
    }

  }
}
