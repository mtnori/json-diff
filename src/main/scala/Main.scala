import filesystem.FileSystemSample
import io.circe.{Decoder, Encoder}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.shapes._
import poiSample.ExcelHandler
import poiSample.ExcelHandler.{
  BooleanToCellValue,
  StringToCellValue,
  DateToCellValue,
  IntToCellValue,
  DoubleToCellValue
}
import sample2.{Bar, Event, Foo}
import utils.Using

object Main extends App {
//  val foo: Foo = Qux(13, Some(14.0))
//
//  val json = foo.asJson.noSpaces
//  println(json)
//
//  val decodedFoo = decode[Foo](json)
//  println(decodedFoo)
//
//  println(Sample.parse1(Sample.successJson))
//
//  Sample.parse1(Sample.failureJson) match {
//    case Left(failure) => println("Invalid Json :(")
//    case Right(json)   => println("Yay, got some JSON!")
//  }
//
//  CursorSample.traversing()
//
//  EncodingSample.execute()
//
//  SemiAutomaticSample.execute()
//
//  AutomaticSample.execute()
//
//  CustomEncodeSample.execute()

//  ADTSample.execute()
//  ShapeADTSample.execute()

  // JSONパースサンプル
  println("================JSON Parse================")
  import sample2.ShapesDerivation._
  println(
    decode[NameWithEventMap](
      """{ "name": "sampleName", "events": { "x": { "value": "Hello" }, "y": { "value": 500  } } }"""
    )
  )
  println(
    NameWithEventMap("sampleName", Map("x" -> Bar("Hello"), "y" -> Foo(500))).asJson.noSpaces
  )

  println("================Find Search================")
  // "D:\\temp\\in"
  // カリー化してあるので、argsで渡された基底パスを部分適用する
  val getJsonFilePathsWithBaseDir =
    FileSystemSample.getZipInnerJsonFilePaths(args(0))
  // New分を取得
  val newJSONs = getJsonFilePathsWithBaseDir("new")
  // Old分を取得
  val oldJSONs = getJsonFilePathsWithBaseDir("old")
  println("================Json Content================")
  // いったん表示してみる
  println(newJSONs)
  println(oldJSONs)

  println("================Parsed Json================")
  // TODO Jsonをデコードして型で扱えるようにする
  val parsedData = decode[NameWithEventMap](newJSONs)
  println(parsedData)

  // TODO 同じtype同士でマッチさせて、追加要素で一致しているか確認。一致したら比較開始。
  val data = parsedData.getOrElse(throw new Exception("parse failed"))

  // TODO 結果をPOIでExcelに書き出したい(出力先はarg(1)で渡す)
  println("================Apache POI================")
  Using.usingResource(ExcelHandler.load("format.xlsx", 0)) { excel =>
    excel.writeCell("aaaaaaa", 0, 5)
    excel.writeCell(1.5, 10, 5)
    excel.writeCell(100, 10, 7)
    excel.writeCellByName("test_name", "TESTNAME")
    // excel.mergeCells(10, 5, 10, 2)
    // excel.writeCell("MergedCell", 10, 10)
    excel.insertRow(0)
    // println(excel.getCellValue(10, 10)) // 結合セルの取得
    ExcelHandler.save(excel, args(1), "save.xlsx")
  }
//  BetterFiles.execute()

//  sealed trait Event
//
//  case class Foo(i: Int) extends Event
//  case class Bar(s: String) extends Event
//  case class Baz(c: Char) extends Event
//  case class Qux(values: List[St  ring]) extends Event
//
//  import io.circe.{Decoder, Encoder}
//  import io.circe.generic.auto._, io.circe.shapes._
//
//  object ShapesDerivation {
//    import shapeless.{Coproduct, Generic}
//
//    implicit def encodeAdtNoDiscr[A, Repr <: Coproduct](
//      implicit
//      gen: Generic.Aux[A, Repr],
//      encodeRepr: Encoder[Repr]
//    ): Encoder[A] = encodeRepr.contramap(gen.to)
//
//    implicit def decodeAdtNoDiscr[A, Repr <: Coproduct](
//      implicit
//      gen: Generic.Aux[A, Repr],
//      decodeRepr: Decoder[Repr]
//    ): Decoder[A] = decodeRepr.map(gen.from)
//  }
//
//  import ShapesDerivation._
//
//  println(io.circe.parser.decode[Event]("""{ "i": 1000 }"""))
}
