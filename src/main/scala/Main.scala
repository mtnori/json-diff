import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path}
import java.util.function.BiPredicate

import filesystem.FileSystemSample
import io.circe.{Decoder, Encoder}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.shapes._
import sample2.{Bar, Event, Foo}

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
  val getJsonfilePathsWithBaseDir =
    FileSystemSample.getZipInnerJsonFilePaths(args(0))
  // New
  val newJSONs = getJsonfilePathsWithBaseDir("new")
  // Old
  val oldJSONs = getJsonfilePathsWithBaseDir("old")
  println(newJSONs)
  println(oldJSONs)

  // TODO Pathをどこかに控えておいて、Json取得時に使う

  // TODO Jsonをまとめて一つの型へまとめる

  // TODO 同じtype同士でマッチさせて、追加要素で一致しているか確認。一致したら比較開始。

  // TODO 結果をPOIでExcelに書き出したい(出力先はarg(1)で渡す)

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
