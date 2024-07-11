package misc
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.{FileInputStream, FileOutputStream}

class ExcelObject {

  def saveToExcel(
      data: List[List[String]],
      headers: List[String],
      fileName: String
  ): Unit = {
    val workbook = new XSSFWorkbook()
    val sheet = workbook.createSheet("Sheet1")

    // Create header row
    val headerRow = sheet.createRow(0)
    headers.zipWithIndex.foreach { case (header, index) =>
      headerRow.createCell(index).setCellValue(header)
    }

    // Create data rows
    data.zipWithIndex.foreach { case (row, i) =>
      val sheetRow = sheet.createRow(i + 1) // Start from the second row
      row.zipWithIndex.foreach { case (cell, j) =>
        sheetRow.createCell(j).setCellValue(cell)
      }
    }

//    val file = "C:\\Users\\shengshan.zhang\\Desktop\\" + fileName + ".xlsx"
    val outputStream = new FileOutputStream(fileName)
    workbook.write(outputStream)
    workbook.close()
    outputStream.close()
  }

  def readExcel(filePath: String): List[List[String]] = {
    val inputStream = new FileInputStream(filePath)
    val workbook = new XSSFWorkbook(inputStream)
    val sheet = workbook.getSheetAt(0)
    val data = for (rowIndex <- 0 until sheet.getLastRowNum) yield {
      val row = sheet.getRow(rowIndex)
      for (cellIndex <- 0 until row.getLastCellNum) yield {
        val cell = row.getCell(cellIndex)
        cell.toString
      }
    }
    workbook.close()
    inputStream.close()
    data.map(_.toList).toList
  }
}
