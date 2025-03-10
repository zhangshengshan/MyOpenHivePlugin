package misc

import action.extract.{
  DorisTablesExtractor,
  MySqlTablesExtractor,
  SparkSqlTablesExtractor
}
import antlr.g4.{SqlBaseLexer, SqlBaseParser}
import antlr4.mysql.{MySqlLexer, MySqlParser}
import com.intellij.openapi.fileChooser.{
  FileChooserDescriptor,
  FileChooserDialog,
  FileChooserFactory
}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import config.os.OsConfig
import doris.{DorisLexer, DorisParser}
import hierachyconfig.MyConfigurable
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.CaseChangingCharStream

object TableExtractUtil {

  def processHiveTables(text: String): Unit = {
    val lexer: SqlBaseLexer =
      new SqlBaseLexer(
        new CaseChangingCharStream(CharStreams.fromString(text), true)
      )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser: SqlBaseParser = new SqlBaseParser(commonTokenStream)
    val context: SqlBaseParser.StatementContext = parser.statement()
    val visitor = new SparkSqlTablesExtractor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()
    if (MyConfigurable.getInstance().isDownloadAfterExtract) {}
    Messages.showInfoMessage(
      plot.mkString("\r"),
      "Extracted Tables"
    )
    ClipBoardUtil.copyToClipBoard(plot.mkString("\r"))
  }
  def processDorisTables(
      text: String,
      newStyle: Option[Boolean] = None
  ): List[(String, String)] = {
    println("SQL")

    // delete the line that contains "truncate"
    val lines = text.split("\n")
    val newLines =
      lines.filterNot(x => x.contains("truncate") || x.contains("TRUNCATE"))
    val newText = newLines.mkString("\n")

    val lexer = new DorisLexer(
      new CaseChangingCharStream(CharStreams.fromString(newText), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser = new DorisParser(commonTokenStream)
    val context: DorisParser.MultiStatementsContext = parser.multiStatements()
    val visitor = new DorisTablesExtractor()

    visitor.visit(context)

    if (newStyle.isDefined) {
      visitor.getDepedence
    } else {
      val plot: List[(String, Int)] = visitor.plot()
      val targetTables =
        visitor.getTargetTables().mkString("tables are:", "\r", "!")
      Messages.showInfoMessage(
        targetTables,
        "Extracted Tables"
      )
      if (MyConfigurable.getInstance().isDownloadAfterExtract) {}
      Messages.showInfoMessage(
        plot.map(x => x._1 + ":" + x._2).mkString("\r"),
        "Extracted Tables"
      )
      Messages.showYesNoDialog(
        "Tables And Frequency Or Only Tables?",
        "Copy to Clipboard",
        Messages.getQuestionIcon
      ) match {
        case Messages.YES =>
          ClipBoardUtil.copyToClipBoard(
            plot.map(x => x._1 + ":" + x._2).mkString("\r")
          )
        case Messages.NO =>
          ClipBoardUtil.copyToClipBoard(plot.map(x => x._1).mkString("\r"))
      }
      val tuples: Set[(String, String)] = visitor.getDepedence.toSet
      Messages.showInfoMessage(
        tuples.map(x => x._1 + ":" + x._2).mkString("\r"),
        "Extracted Tables"
      )
      // TODO : 弹出对话框询问输入文件
      saveDataToExcel(tuples)
      tuples.map(item => (item._2, item._1)).toList
    }
  }

  def saveDataToExcel(tuples: Set[(String, String)]): Any = {
    import javax.swing.{JFileChooser, JOptionPane}
    val fileChooser = new JFileChooser()
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    val result = fileChooser.showOpenDialog(null)

    if (result == JFileChooser.APPROVE_OPTION) {
      val directory = fileChooser.getSelectedFile
      val defaultFileName = "default"
      val fileName =
        JOptionPane.showInputDialog(null, "请输入文件名", defaultFileName)

      // 这里的filePath是用户选择的目录路径和输入的文件名的组合

      val separator = if (SystemInfo.isWindows) "\\" else "/"
      val filePath = directory.getAbsolutePath + separator + fileName + ".xlsx"
      val excel = new ExcelObject
      val list: List[List[String]] =
        tuples.toList.map(x => List(x._1, x._2)).toList
      excel.saveToExcel(list, List("target", "source"), filePath)

      // 打开excel 文件
      if (SystemInfo.isWindows) {
        Runtime.getRuntime.exec("cmd /c start " + filePath)
      } else if (SystemInfo.isMac) {
        Runtime.getRuntime.exec("open " + filePath)
      } else if (SystemInfo.isLinux) {
        Runtime.getRuntime.exec("xdg-open " + filePath)
      }
    }
  }

  def processMySQLTables(text: String) = {
    val lexer = new MySqlLexer(
      new CaseChangingCharStream(CharStreams.fromString(text), true)
    )

    val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
    val parser = new MySqlParser(commonTokenStream)

    val context: MySqlParser.SqlStatementsContext = parser.sqlStatements()
    val visitor = new MySqlTablesExtractor()
    visitor.visit(context)
    val plot: List[String] = visitor.plot()

    if (MyConfigurable.getInstance().isDownloadAfterExtract) {}
    Messages.showInfoMessage(
      plot.mkString(System.lineSeparator()),
      "Extracted Tables"
    )
    plot
  }
}
