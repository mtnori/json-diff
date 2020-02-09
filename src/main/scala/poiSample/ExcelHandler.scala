package poiSample

import java.nio.file.{Files, Path, Paths}
import java.util.Date

import org.apache.poi.ss.usermodel._
import org.apache.poi.ss.util.{CellRangeAddress, CellReference}
import utils.Resource
import utils.Using._

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

sealed trait CellVal
case class StringCellVal(value: String) extends CellVal
case class IntCellVal(value: Int) extends CellVal
case class DoubleCellVal(value: Double) extends CellVal
case class BooleanCellVal(value: Boolean) extends CellVal
case class DateCellVal(value: Date) extends CellVal

/**
  * Excel操作クラス
  * @param workbook ワークブック
  * @param sheetIdx 操作対象シート番号
  */
class ExcelHandler(val workbook: Workbook, sheetIdx: Int = 0) extends Resource {

  var selectedSheet: Sheet = workbook.getSheetAt(sheetIdx)

  // デフォルトスタイルを定義
  val defaultStyle: CellStyle = workbook.createCellStyle()
  val font: Font = workbook.createFont()
  font.setFontHeightInPoints(11)
  font.setFontName("Yu Gothic")
  defaultStyle.setFont(font)

  /**
    * シート番号から操作対象となるシートを選択する
    * @param sheetIdx シート番号(0~)
    */
  def selectSheet(sheetIdx: Int): Unit = {
    this.selectedSheet = workbook.getSheetAt(sheetIdx)
  }

  /**
    * シート名から操作対象となるシートを選択する
    * @param sheetName シート名
    */
  def selectSheetByName(sheetName: String): Unit = {
    this.selectedSheet = workbook.getSheet(sheetName)
  }

  /**
    * 指定した行を返す。指定した行が存在しなければ作成して返す。
    * @param rowIdx 行番号
    * @return 行
    */
  private def getRow(rowIdx: Int): Row = {
    val row = this.selectedSheet.getRow(rowIdx)
    if (row != null) row else this.selectedSheet.createRow(rowIdx)
  }

  /**
    * 指定したセルを返す。指定したセルが存在しなければ作成して返す。
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @return セル
    */
  private def getCell(rowIdx: Int, colIdx: Int): Cell = {
    val row = getRow(rowIdx)
    val cell = row.getCell(colIdx)
    if (cell != null) cell else row.createCell(colIdx)
  }

  /**
    * セルに値を設定する
    * @param cellValue セル値
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCell(cellValue: CellVal,
                rowIdx: Int,
                colIdx: Int,
                style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cellValue match {
      case StringCellVal(value)  => cell.setCellValue(value)
      case IntCellVal(value)     => cell.setCellValue(value.toDouble)
      case DoubleCellVal(value)  => cell.setCellValue(value)
      case BooleanCellVal(value) => cell.setCellValue(value)
      case DateCellVal(value)    => cell.setCellValue(value)
    }
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * 名前定義からセルを探して値をセットする
    * 指定した名前のセルが見つからなければ何もしない
    * @param cellValue セル値
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellByName(cellValue: CellVal,
                      name: String,
                      style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCell(cellValue, ref.getRow, ref.getCol.toInt, style)
    }
  }

  /**
    * セルに計算式を設定する
    * @param formula 計算式
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCellFormula(formula: String,
                       rowIdx: Int,
                       colIdx: Int,
                       style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cell.setCellFormula(formula)
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * 名前定義からセルを探して計算式をセットする
    * @param formula 計算式
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellFormulaByName(formula: String,
                             name: String,
                             style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCellFormula(formula, ref.getRow, ref.getCol.toInt, style)
    }
  }

  /**
    * セルを結合する
    * @param startRowIdx 開始行
    * @param mergeRowNum 行結合数
    * @param startColIdx 開始列
    * @param mergeColNum 列結合数
    */
  def mergeCells(startRowIdx: Int,
                 mergeRowNum: Int,
                 startColIdx: Int,
                 mergeColNum: Int): Int = {
    this.selectedSheet.addMergedRegion(
      new CellRangeAddress(
        startRowIdx,
        startRowIdx + mergeRowNum - 1,
        startColIdx,
        startColIdx + mergeColNum - 1
      )
    )
  }

  /**
    * セルの結合を解除する
    * TODO 結合解除は処理が重いので、mergeCells関数内に組み込まない方が良い
    * @param startRowIdx 開始行
    * @param mergeRowNum 行結合数
    * @param startColIdx 開始列
    * @param mergeColNum 列結合数
    */
  def unMergeCells(startRowIdx: Int,
                   mergeRowNum: Int,
                   startColIdx: Int,
                   mergeColNum: Int): Unit = {

    import scala.jdk.CollectionConverters._
    val targetRange = new CellRangeAddress(
      startRowIdx,
      startRowIdx + mergeRowNum - 1,
      startColIdx,
      startColIdx + mergeColNum - 1
    )
    val mergeList: Seq[CellRangeAddress] =
      this.selectedSheet.getMergedRegions.asScala.toSeq
    val removeIndices: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    for ((mergedAddress, mergeIndex) <- mergeList.zipWithIndex) {
      if (targetRange.intersects(mergedAddress)) {
        removeIndices.append(mergeIndex)
      }
    }
    // 手前から解除するとインデックスが合わなくなるので後ろから消す
    for (removeIndex <- removeIndices.reverse) {
      this.selectedSheet.removeMergedRegion(removeIndex)
    }
  }

  /**
    * セルの計算式の計算結果を取得する
    * @param cell セル
    * @return セルの計算式の計算結果(文字列
    */
  private def getStringFormulaValue(cell: Cell): String = {
    val helper: CreationHelper = this.workbook.getCreationHelper
    val evaluator: FormulaEvaluator = helper.createFormulaEvaluator()
    val value: CellValue = evaluator.evaluate(cell)
    value.getCellType match {
      case CellType.STRING => cell.getStringCellValue
      case CellType.NUMERIC =>
        if (DateUtil.isCellDateFormatted(cell)) cell.getDateCellValue.toString
        else cell.getNumericCellValue.toString
      case CellType.BOOLEAN => cell.getBooleanCellValue.toString
      case CellType.ERROR =>
        val errorCode = cell.getErrorCellValue
        val error: FormulaError = FormulaError.forInt(errorCode)
        error.getString
      case CellType.BLANK => ""
      case CellType._NONE => ""
      case _              => ""
    }
  }

  /**
    * 結合セルの値を取得する
    * 結合セルの場合、値が入っているのは一番左上のセルになる
    * @param cell セル
    * @return セルの値(文字列)
    */
  private def getStringRangeValue(cell: Cell): String = {
    val rowIdx = cell.getRowIndex
    val colIdx = cell.getColumnIndex
    val size: Int = selectedSheet.getNumMergedRegions
    val b = new Breaks
    var result = ""
    b.breakable {
      for (i <- 0 to size) {
        val range: CellRangeAddress = selectedSheet.getMergedRegion(i)
        if (range.isInRange(rowIdx, colIdx)) {
          result = getCellValue(range.getFirstRow, range.getFirstColumn)
          b.break
        }
      }
    }
    result
  }

  /**
    * セルの値を文字列で返す
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @return セルの値(文字列)
    */
  def getCellValue(rowIdx: Int, colIdx: Int): String = {
    val cell: Cell = getCell(rowIdx, colIdx)
    cell.getCellType match {
      case CellType.STRING => cell.getStringCellValue
      case CellType.NUMERIC =>
        if (DateUtil.isCellDateFormatted(cell)) cell.getDateCellValue.toString
        else cell.getNumericCellValue.toString
      case CellType.BOOLEAN => cell.getBooleanCellValue.toString
      case CellType.FORMULA => getStringFormulaValue(cell)
      case CellType.ERROR =>
        val errorCode = cell.getErrorCellValue
        val error: FormulaError = FormulaError.forInt(errorCode)
        error.getString
      case CellType.BLANK => getStringRangeValue(cell)
      case CellType._NONE => ""
      case _              => ""
    }
  }

  /**
    * 行を挿入する
    * その際、スタイルとセル結合情報を開始行からコピーする
    * @param startRowIdx 開始行
    */
  def insertRow(startRowIdx: Int): Unit = {
    // コピー元の行を取得
    val originalRow = getRow(startRowIdx)

    // シート末尾に新しい行を追加
    val lastRowNum = this.selectedSheet.getLastRowNum
    this.selectedSheet.createRow(lastRowNum + 1)

    // 行を追加する位置以降の行を下にずらす
    this.selectedSheet.shiftRows(startRowIdx + 1, lastRowNum, 1)

    // 追加した行にスタイルを設定
    val newRow = getRow(startRowIdx + 1)
    for (colIdx <- 0 until originalRow.getLastCellNum) {
      val originalCell = getCell(startRowIdx, colIdx)
      val newCell = newRow.createCell(colIdx)

      // セルのスタイルをコピー
      val newCellStyle = this.workbook.createCellStyle()
      newCellStyle.cloneStyleFrom(originalCell.getCellStyle)
      newCell.setCellStyle(newCellStyle)

      // FIXME Deprecated POI 5.0で廃止予定
      // セルタイプのコピー
      // newCell.setCellType(originalCell.getCellType)

      // 追加した行にマージ状態を設定
      if (this.selectedSheet.getNumMergedRegions > 0) {

        val b = new Breaks
        b.breakable {
          for (index <- 0 until this.selectedSheet.getNumMergedRegions) {
            val cellRangeAddress = this.selectedSheet.getMergedRegion(index)
            // コピー元セル(結合済み)の左上セルがmergedRegionと一致すれば、
            // 新しいセルに対してmergeRegionを追加する
            if (cellRangeAddress.getFirstRow == originalCell.getRowIndex
                && cellRangeAddress.getFirstRow == originalCell.getColumnIndex) {
              this.selectedSheet.addMergedRegion(
                new CellRangeAddress(
                  newRow.getRowNum,
                  newRow.getRowNum + (cellRangeAddress.getLastRow - cellRangeAddress.getFirstRow),
                  cellRangeAddress.getFirstColumn,
                  cellRangeAddress.getLastColumn
                )
              )
              b.break
            }
          }
        }
      }
    }
  }

  /**
    * 指定した行の高さを設定する
    * @param rowIdx 行番号
    * @param n デフォルトの高さの何倍に設定するか
    */
  def setRowHeight(rowIdx: Int, n: Int): Unit = {
    val row = getRow(rowIdx)
    row.setHeightInPoints(n * selectedSheet.getDefaultRowHeightInPoints)
  }

  /**
    * ワークブックを開放する
    */
  def close(): Unit = {
    if (this.workbook != null) {
      this.workbook.close()
    }
  }
}

object ExcelHandler {

  // 暗黙の型変換で、各型をCellValue型として扱うことで、オーバーロードメソッドを減らす
  import scala.language.implicitConversions
  implicit def StringToCellValue(value: String): StringCellVal =
    StringCellVal(value)
  implicit def DoubleToCellValue(value: Double): DoubleCellVal =
    DoubleCellVal(value)
  implicit def IntToCellValue(value: Int): IntCellVal =
    IntCellVal(value)
  implicit def BooleanToCellValue(value: Boolean): BooleanCellVal =
    BooleanCellVal(value)
  implicit def DateToCellValue(value: Date): DateCellVal =
    DateCellVal(value)

  /**
    * 雛形ファイルを読み込む
    * resources/formats下のファイルを指定することができる
    * @param format 雛形ファイル名
    * @param sheetIdx シート番号
    * @return Excelデータ
    */
  def load(format: String, sheetIdx: Int): ExcelHandler = {
    usingAutoCloseable(
      getClass.getClassLoader.getResourceAsStream(s"formats/$format")
    ) { inputStream =>
      {
        new ExcelHandler(WorkbookFactory.create(inputStream))
      }
    }
  }

  /**
    * ファイルを保存する
    * @param excelHandler Excelデータ
    * @param outputDir 出力先フォルダ
    * @param fileName ファイル名
    * @return 作成したファイルのパス
    */
  def save(excelHandler: ExcelHandler,
           outputDir: String,
           fileName: String): Path = {
    val outputPath = Paths.get(outputDir).resolve(fileName)
    usingAutoCloseable(Files.newOutputStream(outputPath)) { outputStream =>
      {
        excelHandler.workbook.write(outputStream)
        outputStream.flush()
        outputPath
      }
    }
  }
}
