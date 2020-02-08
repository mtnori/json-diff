import io.circe.{Decoder, Encoder}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.shapes._
import sample2.{Event, Foo}

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

  import sample2.ShapesDerivation._
  println(decode[Event]("""{ "s": "Hello" }"""))
  println((Foo(100): Event).asJson.noSpaces)

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
