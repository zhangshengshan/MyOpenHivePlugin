package action.fieldmatch
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.awt.Desktop
import java.io.{File, FileOutputStream}

object ExcelUtil {
  def createAndOpenExcel(filePath: String): Unit = {
    // Create a new workbook and sheet
    val workbook: Workbook = new XSSFWorkbook()
    val sheet: Sheet = workbook.createSheet("Sheet1")

    // Create a row and put some cells in it
    val headerRow: Row = sheet.createRow(0)
    val headers =
      Array("sourceTable", "sourceField", "targetTable", "targetField")
    for (i <- headers.indices) {
      val cell: Cell = headerRow.createCell(i)
      cell.setCellValue(headers(i))
    }

    // Write the output to a file
    val fileOut = new FileOutputStream(filePath)
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()

    // Open the file using the default application
    val file = new File(filePath)
    if (Desktop.isDesktopSupported && file.exists()) {
      Desktop.getDesktop.open(file)
    } else {
      println(s"File $filePath cannot be opened automatically.")
    }
  }
}

// Usage
