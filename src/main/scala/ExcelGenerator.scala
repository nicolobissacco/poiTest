import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.time.{Duration, LocalDateTime}

object ExcelGenerator extends App {
  val fmt = DateTimeFormatter.ofPattern("HH:mm:ss")
  val headers: Seq[String] = for (i <- 0 until 70) yield "some awesome example data" //s"ID-${i}"
  val products: Seq[Map[String, String]] = for (i <- 0 until 100000) yield (for (c <- 0 until headers.length) yield headers(c) -> "some awesome example data").toMap //s"desc-P${i}-${c}"

  generateXlsx("file.xlsx", headers, products)

  def generateXlsx = (filePath: String, headers: Seq[String], items: Seq[Map[String, String]]) => {
    val t1 = LocalDateTime.now()
    println(s"START ${t1.format(fmt)}")

    val wb = new org.apache.poi.xssf.streaming.SXSSFWorkbook(org.apache.poi.xssf.streaming.SXSSFWorkbook.DEFAULT_WINDOW_SIZE)
    //val wb = new XSSFWorkbook
    val sheet1 = wb.createSheet("Foglio1")

    createHeader(wb, sheet1, headers)
    createRows(wb, sheet1, headers, items)
    saveToFile(wb, filePath);
    wb.dispose

    val t2 = LocalDateTime.now()
    println(s"END ${t2.format(fmt)}")
    println(s"DIFF ${Duration.between(t1, t2).getSeconds}")
  }

  def createRows = (wb: org.apache.poi.ss.usermodel.Workbook, sheet: org.apache.poi.ss.usermodel.Sheet, headers: Seq[String], items: Seq[Map[String, String]]) => {
    for (r <- 0 until items.length; c <- 0 until headers.length) {
      val row = {
        if (sheet.getRow(r + 1) == null) {
          sheet.createRow(r + 1)
        }
        else {
          sheet.getRow(r + 1)
        }
      }
      row.createCell(c).setCellValue(items(r).getOrElse(headers(c), ""))
    }
  }

  def createHeader = (wb: org.apache.poi.ss.usermodel.Workbook, sheet: org.apache.poi.ss.usermodel.Sheet, headers: Seq[String]) => {
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

  def saveToFile = (wb: org.apache.poi.ss.usermodel.Workbook, filePath: String) => {
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