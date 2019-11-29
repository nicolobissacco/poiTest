import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelGenerator extends App {
  val headers: Seq[(String, String)] = for (i <- 0 until 70) yield (s"ID-${i}", s"DESC-${i}")
  val products: Seq[Map[String, String]] = for (i <- 0 until 10000) yield (for (c <- 0 until headers.length) yield headers(c)._1 -> s"desc-P${i}-${c}").toMap

  generateXlsx("file.xlsx", headers, products)

  def generateXlsx = (filePath: String, headers: Seq[(String, String)], items: Seq[Map[String, String]]) => {
    val fmt = DateTimeFormatter.ofPattern("HH:mm:ss")
    println(s"START ${LocalDateTime.now().format(fmt)}")

    val wb = new XSSFWorkbook
    val sheet1 = wb.createSheet("Foglio1")

    createHeader(wb, sheet1, headers.map(x => x._2))
    createRows(wb, sheet1, headers.map(x => x._1), items)
    saveToFile(wb, filePath);

    println(s"END ${LocalDateTime.now().format(fmt)}")
  }

  def createRows = (wb: XSSFWorkbook, sheet: Sheet, headers: Seq[String], items: Seq[Map[String, String]]) => {
    for (r <- 0 until items.length; c <- 0 until headers.length) {
      var row = sheet.getRow(r + 1)
      if (row == null) {
        row = sheet.createRow(r + 1)
      }
      row.createCell(c).setCellValue(items(r).getOrElse(headers(c), ""))
    }
  }

  def createHeader = (wb: XSSFWorkbook, sheet: Sheet, headers: Seq[String]) => {
    val boldStyle = wb.createCellStyle
    val boldFont = wb.createFont
    boldFont.setBold(true)
    boldStyle.setFont(boldFont)

    val headerRow = sheet.createRow(0)
    for (ch <- 0 until headers.length) {
      val cell = headerRow.createCell(ch)
      cell.setCellStyle(boldStyle)
      cell.setCellValue(headers(ch))
    }
  }

  def saveToFile = (wb: XSSFWorkbook, filePath: String) => {
    val fos = new FileOutputStream(filePath)
    wb.write(fos)
    fos.close()
  }
}

/*
ELASTIC4S
search("electronics" / "phone").query(idsQuery("foo"))
search in IndexType query {
      termsQuery("_id", ids: _* )
    }
 */