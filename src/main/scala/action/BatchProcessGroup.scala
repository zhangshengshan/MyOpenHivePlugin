package action

import _root_.util.OpenFileUtil
import action.extract.DorisTableModifier
import action.util.{EditorUtil, ExceptionHandle}
import com.intellij.notification.{Notification, NotificationType, Notifications}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys, DefaultActionGroup}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.{FileChooserDescriptor, FileChooserDialog, FileChooserFactory}
import com.intellij.openapi.project.{Project, ProjectManager}
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import config.os.OsConfig
import doris.{DorisLexer, DorisParser}
import misc.ClipBoardUtil
import mydata.studio.{DataStudioCommonParam, MyDataStudio}
import myui.MyMultiChoiceDialog
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

import java.io.{File, FileOutputStream}
import java.util

class BatchProcessGroup extends DefaultActionGroup {
  override def getChildren(e: AnActionEvent): Array[AnAction] = {
    val quoteWrapperMenu =
      new DefaultActionGroup("添加引号", true) // 第二个参数表示这是一个可以展开的菜单
    quoteWrapperMenu.add(new SingleQuoteWrapper)
    quoteWrapperMenu.add(new DoubleQuoteWrapper)
    // 可以继续添加更多的子菜单项
    val tableMetaMenu = new DefaultActionGroup("表格元数据", true)
    tableMetaMenu.add(new SaveDorisMetaToXlsx)
    tableMetaMenu.add(new CompareTwoTables)

    Array(
      quoteWrapperMenu, // 将子菜单添加到主菜单中
      tableMetaMenu,
      new CommentProcess,
      new DDLFixer,
      new SearchTable,
      new CreateTableActioin,
      new JoinConditionExtractor,
      new ClipBoardHistoryAction
    )
  }

}

/** 关联条件提取器类，继承自AnAction类，允许用户通过剪贴板提取关联条件
 *
 * @extends AnAction 表示该类继承了AnAction类，并且指定了在用户界面中的显示名称为“关联条件”
 */
class JoinConditionExtractor extends AnAction("关联条件") {

  /** 当动作被触发时执行的方法从剪贴板中提取关联条件并显示通知
   *
   * @param e 事件对象，包含了事件的相关信息，如上下文等
   */
  override def actionPerformed(e: AnActionEvent): Unit = {
    // 从剪贴板获取内容
    val clipboard = ClipBoardUtil.getFromClipboard
    // 定义一个模式，用于匹配表名和列名
    val pattern = """\b(\w+)\.(\w+)\b""".r
    // 在剪贴板内容中，根据模式替换表名，只保留列名
    val str = pattern.replaceAllIn(clipboard, m => m.group(2))
    // 处理字符串，移除“ON”关键字和前后空白，提取干净的关联条件
    val result =
      str.replaceAll("\\bON\\b", "").replaceAll("\\bon\\b", "").strip()
    // 在这里把行首的空白字符也替换掉（代码中未体现，但注释建议处理空白字符）

    // 创建一个通知对象，用于显示提取的关联条件
    val notification = new Notification(
      "JoinConditionExtractor",
      "剪切板内容",
      result,
      NotificationType.INFORMATION
    )
    // 发送通知
    Notifications.Bus.notify(notification)
    // 将提取的关联条件复制回剪贴板
    ClipBoardUtil.copyToClipBoard(result)
  }
}

class SingleQuoteWrapper extends AnAction("单引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val str: Array[String] = clipboard
      .split("\n")

    val result = Messages.showYesNoDialog(
      "是否继续?",
      "确认表示单引号否则是双引号",
      Messages.getWarningIcon
    )
    val split_char = if (result == Messages.YES) {
      "'"
    } else {
      "\""
    }

    val wrapper =
      Messages.showYesNoDialog("请输入包装符号", "确认是()还是[]", Messages.getQuestionIcon)

    val wrapper_char = if (wrapper == Messages.YES) {
      ("(", ")")
    } else {
      ("[", "]")
    }

    val ret =
      if (str.length > 1)
        str
          .map(x => x.strip())
          .filter(x => x != "")
          .map(x => split_char + x.strip() + split_char)
          .mkString(wrapper_char._1, ",", wrapper_char._2)
      else split_char + str(0) + split_char
    Messages.showInfoMessage(ret, "剪切板内容")
    ClipBoardUtil.copyToClipBoard(ret)
  }
}

class DoubleQuoteWrapper extends AnAction("双引号") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboard = ClipBoardUtil.getFromClipboard
    var params: util.List[String] = new util.ArrayList[String]()
    try {

      val myDataStudio = MyDataStudio.getInstance()
      // 这里我希望能够获取MyDataStudio实例中的成员该如何写 ？
      val inputParamObj = new DataStudioCommonParam(myDataStudio)
      params = inputParamObj.getParams

    } catch {
      case e: Throwable =>
        e.printStackTrace()
        Messages.showMessageDialog(
          e.getMessage,
          "Error",
          Messages.getErrorIcon
        )
        params = new util.ArrayList[String]()
    }

    import scala.collection.JavaConverters._

    val a =
      if (params.size == 1)
        Messages.showEditableChooseDialog(
          "选择Filter字段",
          "字段选择（不选择表示仅生成列表）",
          null,
          params.asScala.toArray,
          params.get(0),
          null
        )
      else {
        Messages.showEditableChooseDialog(
          "选择Filter字段",
          "字段选择（不选择表示仅生成列表）",
          null,
          params.asScala.toArray,
          null,
          null
        )
      }

    val str = clipboard
      .split("\n")
      .map(x => "\"" + x.strip() + "\"")
      .mkString("(", ",", ")")

    val clipBoardStr = if (a != null) {
      a + " IN " + str
    } else {
      str
    }

    val sqlSelectParts = ClipBoardUtil.getClipboardHistory

    try {
      val sql =
        if (sqlSelectParts.size >= 2) {
          Messages.showEditableChooseDialog(
            "选择需要前置的查询SELECT部分",
            "字段选择（不选择表示仅生成列表）",
            null,
            sqlSelectParts.tail.toArray.map(_.split("\n").mkString("\r")),
            null,
            null
          )
        } else {
          null
        }
      Messages.showInfoMessage(
        sql
          .split(System.lineSeparator())
          .mkString(System.lineSeparator()) + "WHERE" + System
          .lineSeparator() + clipBoardStr,
        "剪切板内容"
      )
    } catch {
      case _ =>
        Messages.showInfoMessage(clipBoardStr, "剪切板内容")
        ClipBoardUtil.copyToClipBoard(clipBoardStr)
    }

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

      // 此处想要生成两个表格不一样的行，但是不知道怎么做
      // 先选择唯一键，然后再进行比较

      // 此处弹出一个对话框提示用户是否要继续
      val ret = Messages.showYesNoDialog("是否继续?", "确认", Messages.getWarningIcon)
      if (ret == Messages.YES) {
        // 用户点击了 "Yes"
      } else if (ret == Messages.NO) {
        // 用户点击了 "No"
        return
      }

      import scala.jdk.CollectionConverters._

      val scalaList: List[String] = bothSids.toList
      val javaList: java.util.List[String] = scalaList.asJava

      val dialog = new MyMultiChoiceDialog(javaList)
      dialog.show()

      if (dialog.isOK) {
        val values: List[String] =
          dialog.getSelectedOptions.asScala.toList.distinct

        Messages.showInfoMessage(values.mkString(","), "选择的字段")

        val OnPart: String = values
          .map(x => {
            s"a.${x} = b.${x}"
          })
          .mkString("ON\n", " \nAND\n ", "")

        val wherePart = "\nWHERE\n" + bothSids
          .map(x => {
            s"COALESCE(a.${x},'') != COALESCE(b.${x},'')"
          })
          .mkString("\n(\n", " \nOR\n ", ")") + "\n OR \n" + values
          .map(x => {
            s"\n a.${x} is null \nOR\n b.${x} is null"
          })
          .mkString("(", " OR ", ")")

        val outer = sourceTableMeta.get.data.properties
          .filter(x => bothSids.contains(x.name))
          .map(item => {
            s"a.${item.name} <> b.${item.name} AS is_diff_${item.name}"
          })
          .mkString(
            ",\n"
          )

        val sstr = sourceTableMeta.get.data.properties
          .filter(x => bothSids.contains(x.name))
          .map(item => {
            s"${item.name} AS ${item.name}"
          })
          .mkString(
            "SELECT\n",
            ",\n",
            s" FROM ${sourceTable}"
          )
        val tstr = targetTableMeta.get.data.properties
          .filter(x => bothSids.contains(x.name))
          .map(item => s"${item.name} AS ${item.name}")
          .mkString(
            "SELECT\n",
            ",\n",
            s" FROM ${targetTable}"
          )

        val finalStr = " SELECT \n " + outer + " \n" +
          s" FROM (${sstr}) AS a \n" +
          s" JOIN (${tstr}) AS b \n" + OnPart + wherePart

        Messages.showInfoMessage(finalStr, "最终得到的SQL")
        ClipBoardUtil.copyToClipBoard(finalStr)
      }
    } catch {
      case e: Throwable =>
        ExceptionHandle.handleException(e)

    }
  }
}

class SaveDorisMetaToXlsx extends AnAction("保存元数据") {

  import org.apache.poi.ss.usermodel.{Cell, Row, Sheet, Workbook}
  import org.apache.poi.xssf.usermodel.XSSFWorkbook

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

    val columnsList = responseObj.data.properties.map(x => x.name).mkString(",") + ",ts_ms,op,`table`"
    val jsonPath = responseObj.data.properties.map(x => "\\\"$.after." + x.name + "\\\"").mkString(",") + "\\\"$.source.ts_ms\\\",\\\"$.op\\\",\\\"$.source.table\\\""
    Messages.showInfoMessage(columnsList, "Copy Columns")
    ClipBoardUtil.copyToClipBoard(columnsList)
    Messages.showInfoMessage(jsonPath, "Copy JSONPath")
    ClipBoardUtil.copyToClipBoard(jsonPath)

    listRowIdx += 3
//    val columnlistRow = sheet.createRow(1)
//    val columnlistCell0: Cell = columnlistRow.createCell(0)
//    columnlistCell0.setCellValue(columnsList)

    listRowIdx += 1
//    val jsonPathRow = sheet.createRow(1)
//    val jsonPathCell0: Cell = jsonPathRow.createCell(0)
//    jsonPathCell0.setCellValue(jsonPath)




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

    val initialFileName = tableList.map(x => x.split("\\.")(1)).mkString("_")
    val fileName: String = Messages.showInputDialog(
      project,
      "请输入文件名",
      "文件名",
      Messages.getQuestionIcon,
      initialFileName,
      null
    )

    tableList.foreach(table => {
      val yourdb = table.split("\\.")(0)
      val yourtb = table.split("\\.")(1)
      Messages.showInfoMessage(
        s"正在处理 $yourdb.$yourtb",
        s"正在处理 $yourdb.$yourtb"
      )
      val responseObj: Response = misc.DorisHttpUtil
        .getTableMeta(user, password, host, port, yourdb, yourtb) match {
        case Some(result) => result
        case None => return
      }


      getXlsxFileFromDoris(responseObj, workbook, yourdb, yourtb, listsheet)
    })

    val filePathName = outPutDir + File.separator + fileName + ".xlsx"
    val fileOut: FileOutputStream = new FileOutputStream(
      filePathName
    )
    workbook.write(fileOut)
    fileOut.close()
    // Closing the workbook
    workbook.close()

    // 在此处打开刚刚保存的文件

    OpenFileUtil.openFileDispatchOsSystem(
      filePathName
    )
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

    val table_list =
      if (
        EditorUtil.getCaretUnderCursor(e.getData(CommonDataKeys.EDITOR)) != null
      ) {
        EditorUtil
          .getCaretUnderCursor(e.getData(CommonDataKeys.EDITOR))
          .split("\n")
          .toList
      } else {
        clipboard.strip().split("\n").toList
      }
    try {
      processDorisSchema(
        table_list,
        user,
        password,
        host,
        port,
        project
      )
      Messages.showInfoMessage("保存成功", "保存成功")
    } catch {
      case e: Throwable =>
        ExceptionHandle.handleException(e)
    }
  }

}

class CreateTableActioin extends AnAction("建表语句") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
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

// 定义一个名为DDLFixer的类，继承自AnAction，显示名称为"DDL修正"
class DDLFixer extends AnAction("DDL修正") {

  // 重写actionPerformed方法，该方法在用户执行该操作时被调用
  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {

    // 获取当前文件的内容
    val editor: Editor = anActionEvent.getData(CommonDataKeys.EDITOR)
    val text: String = editor.getDocument.getText

    // 使用Doris处理文本

    // 创建DorisLexer实例，用于将输入文本转换为词法符号序列
    val lexer = new DorisLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )

    // 创建CommonTokenStream实例，用于存储词法符号的流
    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    // 创建DorisParser实例，用于解析词法符号流
    val parser: DorisParser = new DorisParser(commonTokenStream)

    // 调用parser的multiStatements方法，开始解析输入的DDL语句
    val context = parser.multiStatements()

    // 创建TokenStreamRewriter实例，用于修改词法符号流
    val tokenStreamRewriter =
      new org.antlr.v4.runtime.TokenStreamRewriter(commonTokenStream)
    // 创建DorisTableModifier实例，用于修改表名等
    val visitor = new DorisTableModifier(tokenStreamRewriter)
    // 访问解析树，进行必要的修改
    visitor.visit(context)
    // 获取修改后的DDL语句文本
    val newText = tokenStreamRewriter.getText()
    // 显示修正后的DDL语句
    Messages.showInfoMessage(newText, "修正后的DDL")
    // 将修正后的DDL语句复制到剪贴板
    ClipBoardUtil.copyToClipBoard(newText)
    // 是否要用newText去覆盖文件
    val result =
      Messages.showYesNoDialog("是否覆盖当前文件?", "确认", Messages.getWarningIcon)

    // 根据用户选择是否覆盖文件
    if (result == Messages.YES) {
      // 用户点击了 "Yes"，覆盖当前文件
      ApplicationManager.getApplication.runWriteAction(new Runnable {
        override def run(): Unit = {
          editor.getDocument.setText(newText)
        }
      })
    } else if (result == Messages.NO) {
      // 用户点击了 "No"，不做任何操作
      return
    }
  }
}

class SearchTable extends AnAction("模糊搜索表格") {

  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {
    val (clipboard, host, port, user, password, project) =
      misc.GetConfig.getConfig(anActionEvent)

    Messages.showInfoMessage(clipboard, "剪切板内容")
    val sourceTable = clipboard.strip()
    val sourceTableMeta = misc.DorisHttpUtil.getTableMeta(
      user,
      password,
      host,
      port,
      sourceTable.split("\\.")(0),
      sourceTable.split("\\.")(1)
    )
    val sourceTableMetaMap =
      sourceTableMeta.get.data.properties.map(x => (x.name, x.`type`)).toMap

    val searchCotent =
      Messages.showInputDialog("请输入搜索内容", "搜索内容", Messages.getQuestionIcon)

    val result =
      "select * from \n" + sourceTable + "\nwhere\n" + sourceTableMetaMap.keys
        .map(x => {
          s"COALESCE(CAST(${x} AS STRING), '') like '%${searchCotent}%'"
        })
        .mkString("\nOR\n")
    Messages.showInfoMessage(result, "搜索结果")
    ClipBoardUtil.copyToClipBoard(result)
  }
}

class ClipBoardHistoryAction extends AnAction("查看剪切板历史") {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val clipboardHistory = ClipBoardUtil.getClipboardHistory.map(x => x + "\n")
    val message = clipboardHistory.mkString("\n")
    //    Messages.showInfoMessage(message, "剪切板历史")

    // chose one of the following options
    val selected: String = Messages.showEditableChooseDialog(
      "Choose one of the following options",
      "Choose one of the following options",
      Messages.getInformationIcon,
      clipboardHistory.toArray,
      clipboardHistory.head,
      null
    )

    if (selected != null) {
      // copy to editor in current cursor position
      val editor = e.getRequiredData(CommonDataKeys.EDITOR)
      val document = editor.getDocument
      val caretModel = editor.getCaretModel
      val offset = caretModel.getOffset
      val project = ProjectManager.getInstance().getOpenProjects.head
      WriteCommandAction.runWriteCommandAction(
        project,
        new Runnable {
          override def run(): Unit = {
            // Your document modification code here
            editor.getDocument.insertString(offset, selected)
          }
        }
      )
    }
  }
}
