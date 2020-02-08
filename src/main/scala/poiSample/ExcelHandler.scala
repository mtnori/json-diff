package poiSample

import scala.jdk.CollectionConverters._
import java.io.OutputStream
import java.util.Date

import org.apache.poi.ss.usermodel._
import org.apache.poi.ss.util.{CellRangeAddress, CellReference}

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

/**
  * Excel操作
  * @param workbook ワークブック
  * @param sheetIndex シート番号
  */
case class ExcelHandler(workbook: Workbook, sheetIndex: Int = 0) {
  var sheet: Sheet = workbook.getSheetAt(sheetIndex)
  var defaultStyle: CellStyle = workbook.createCellStyle()

  val font: Font = workbook.createFont()
  font.setFontHeight(11)
  font.setFontName("ＭＳ Ｐゴシック")
  defaultStyle.setFont(font)

  /**
    * ワークブックを保存する
    * @param outputStream 出力ストリーム
    */
  def save(outputStream: OutputStream): Unit = {
    this.workbook.write(outputStream)
  }

  /**
    * 指定した行を返す。指定した行が存在しなければ作成して返す。
    * @param rowIdx 行番号
    * @return 行
    */
  private def getRow(rowIdx: Int): Row = {
    val row = this.sheet.getRow(rowIdx)
    if (row != null) row else this.sheet.createRow(rowIdx)
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
    * @param value 値(String型)
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCell(value: String,
                rowIdx: Int,
                colIdx: Int,
                style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cell.setCellValue(value)
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * セルに値を設定する
    * @param value 値(Double型)
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCell(value: Double,
                rowIdx: Int,
                colIdx: Int,
                style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cell.setCellValue(value)
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * セルに値を設定する
    * @param value 値(Date型)
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCell(value: Date,
                rowIdx: Int,
                colIdx: Int,
                style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cell.setCellValue(value)
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * セルに値を設定する
    * @param value 値(Boolean型)
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCell(value: Boolean,
                rowIdx: Int,
                colIdx: Int,
                style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cell.setCellValue(value)
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * 名前定義からセルを探して値をセットする
    * @param value 値(String型)
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellByName(value: String,
                      name: String,
                      style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCell(value, ref.getRow, ref.getCol.toInt, style)
    }
  }

  /**
    * 名前定義からセルを探して値をセットする
    * @param value 値(Double型)
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellByName(value: Double,
                      name: String,
                      style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCell(value, ref.getRow, ref.getCol.toInt, style)
    }
  }

  /**
    * 名前定義からセルを探して値をセットする
    * @param value 値(Date型)
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellByName(value: Date,
                      name: String,
                      style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCell(value, ref.getRow, ref.getCol.toInt, style)
    }
  }

  /**
    * 名前定義からセルを探して値をセットする
    * @param value 値(Boolean型)
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellByName(value: Boolean,
                      name: String,
                      style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCell(value, ref.getRow, ref.getCol.toInt, style)
    }
  }

  /**
    * セルに計算式を設定する
    * @param value 計算式
    * @param rowIdx 行番号
    * @param colIdx 列番号
    * @param style セルスタイル
    */
  def writeCellFormula(value: String,
                       rowIdx: Int,
                       colIdx: Int,
                       style: CellStyle = defaultStyle): Unit = {
    // セルを取得する
    val cell: Cell = getCell(rowIdx, colIdx)
    // セルの値を設定する
    cell.setCellFormula(value)
    // セルのスタイルを設定する
    cell.setCellStyle(style)
  }

  /**
    * 名前定義からセルを探して計算式をセットする
    * @param value 計算式
    * @param name セルの名前定義
    * @param style セルスタイル
    */
  def writeCellFormulaByName(value: Boolean,
                             name: String,
                             style: CellStyle = defaultStyle): Unit = {
    val lName = this.workbook.getName(name)
    if (lName != null) {
      val ref = new CellReference(lName.getRefersToFormula)
      writeCell(value, ref.getRow, ref.getCol.toInt, style)
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
    this.sheet.addMergedRegion(
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
    * TODO 処理が遅いので、mergeCells関数内に組み込まない
    * @param startRowIdx 開始行
    * @param mergeRowNum 行結合数
    * @param startColIdx 開始列
    * @param mergeColNum 列結合数
    */
  def unMergeCells(startRowIdx: Int,
                   mergeRowNum: Int,
                   startColIdx: Int,
                   mergeColNum: Int): Unit = {
    val targetRange = new CellRangeAddress(
      startRowIdx,
      startRowIdx + mergeRowNum - 1,
      startColIdx,
      startColIdx + mergeColNum - 1
    )
    val mergeList: Seq[CellRangeAddress] =
      this.sheet.getMergedRegions.asScala.toSeq
    val removeIndices: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    for ((mergedAddress, mergeIndex) <- mergeList.zipWithIndex) {
      if (targetRange.intersects(mergedAddress)) {
        removeIndices.append(mergeIndex)
      }
    }
    // 手前から解除するとインデックスが合わなくなるので後ろから消す
    for (removeIndex <- removeIndices.reverse) {
      this.sheet.removeMergedRegion(removeIndex)
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
    val size: Int = sheet.getNumMergedRegions
    val b = new Breaks
    var result = ""
    b.breakable {
      for (i <- 0 to size) {
        val range: CellRangeAddress = sheet.getMergedRegion(i)
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
    val lastRowNum = this.sheet.getLastRowNum
    this.sheet.createRow(lastRowNum + 1)

    // 行を追加する位置以降の行を下にずらす
    this.sheet.shiftRows(startRowIdx + 1, lastRowNum, 1)

    // 追加した行にスタイルを設定
    val newRow = getRow(startRowIdx + 1)
    for (colIdx <- 0 until originalRow.getLastCellNum) {
      val originalCell = originalRow.getCell(colIdx)
      val newCell = newRow.createCell(colIdx)

      // セルのスタイルをコピー
      val newCellStyle = this.workbook.createCellStyle()
      newCellStyle.cloneStyleFrom(originalCell.getCellStyle)
      newCell.setCellStyle(newCellStyle)

      // FIXME Deprecated
      // セルタイプのコピー
      // newCell.setCellType(originalCell.getCellType)

      // 追加した行にマージ状態を設定
      if (this.sheet.getNumMergedRegions > 0) {

        val b = new Breaks
        b.breakable {
          for (index <- 0 until this.sheet.getNumMergedRegions) {
            val cellRangeAddress = this.sheet.getMergedRegion(index)
            // コピー元セル(結合済み)の左上セルがmergedRegionと一致すれば、
            // 新しいセルに対してmergeRegionを追加する
            if (cellRangeAddress.getFirstRow == originalCell.getRowIndex
                && cellRangeAddress.getFirstRow == originalCell.getColumnIndex) {
              this.sheet.addMergedRegion(
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
    row.setHeightInPoints(n * sheet.getDefaultRowHeightInPoints)
  }
}
