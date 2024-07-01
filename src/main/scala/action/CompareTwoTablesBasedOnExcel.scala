package action

import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.{Document, Editor, SelectionModel}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import misc.ClipBoardUtil
import mydata.studio.MyDataStudio
import myui.MyMultiChoiceDialog
import org.apache.poi.ss.usermodel.{
  Cell,
  DataFormatter,
  Header,
  Row,
  WorkbookFactory
}

import java.io.FileInputStream
import javax.swing.JTable
import scala.collection.mutable
import scala.jdk.CollectionConverters.{CollectionHasAsScala, SeqHasAsJava}

class CompareTwoTablesBasedOnExcel extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    // 获取编辑器选择的行，然后在行首添加 -- 进行注释
    val studio = MyDataStudio.getInstance()
    val file = studio.getInputFile
    if (file == null) {
      Messages.showInfoMessage("Please select a file first", "Error")
      return
    }
    val inputStream = new FileInputStream(file)
    val workbook = WorkbookFactory.create(inputStream)
    val sheet = workbook.getSheetAt(0)

    // 如果sheet的行数少于2 ， 报错
    if (sheet.getPhysicalNumberOfRows < 2) {
      Messages.showInfoMessage(
        "The excel file must have at least 2 rows",
        "Error"
      )
      return
    }

    // 如果sheet的列数不等于2 ， 报错
    if (sheet.getRow(0).getPhysicalNumberOfCells != 2) {
      Messages.showInfoMessage("The excel file must have 2 columns", "Error")
      return
    }
    // 第一行第一列为sourceTable , 第一行第二列为targetTable
    val sourceTable = sheet.getRow(0).getCell(0).getStringCellValue
    val targetTable = sheet.getRow(0).getCell(1).getStringCellValue
    val fieldMap = scala.collection.mutable.Map[String, String]()
    for (i <- 1 until sheet.getPhysicalNumberOfRows) {
      val row = sheet.getRow(i)
      val sourceField = row.getCell(0).getStringCellValue
      val targetField = row.getCell(1).getStringCellValue
      fieldMap.put(sourceField, targetField)
    }

    fieldMap.foreach { case (sourceField, targetField) =>
      println(s"$sourceField -> $targetField")
    }

    val tuples = fieldMap.toList
      .map(x => {
        (x._1 + "_" + x._2, x._1, x._2)
      })
      .toList

    val bothSids = tuples.map(_._1).toList

    val javaList: java.util.List[String] = tuples.map(x => x._1).asJava
    val dialog = new MyMultiChoiceDialog(javaList)
    dialog.show()
    if (dialog.isOK()) {
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
          s"a.${x} != b.${x}"
        })
        .mkString("\n(\n", " \nOR\n ", ")") + "\n OR \n" + values
        .map(x => {
          s"\n a.${x} is null \nOR\n b.${x} is null"
        })
        .mkString("(", " OR ", ")")

      val outer = bothSids
        .map(item => {
          s" a.${item} AS ${item}, b.${item} AS ${item} "
        })
        .mkString(
          ",\n"
        )

      val sstr = tuples
        .map(item => {
          s"${item._2} AS ${item._1}"
        })
        .mkString(
          "SELECT\n",
          ",\n",
          s" FROM ${sourceTable}"
        )
      val tstr = tuples
        .map(item => s"${item._3} AS ${item._1}")
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
  }
}
