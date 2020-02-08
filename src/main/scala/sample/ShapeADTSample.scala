package sample

import cats.syntax.functor._
import io.circe.{Decoder, Encoder}, io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import io.circe.shapes
import shapeless.{Coproduct, Generic}

// ADT
sealed trait Expression
case class FooExpression(i: Int) extends Expression
case class BarExpression(x: String) extends Expression

object ShapeADTSample {

  implicit def encodeAdtNoDiscr[A, Repr <: Coproduct](
    implicit
    gen: Generic.Aux[A, Repr],
    encodeRepr: Encoder[Repr]
  ): Encoder[A] = encodeRepr.contramap(gen.to)

  implicit def decodeAdtNoDiscr[A, Repr <: Coproduct](
    implicit
    gen: Generic.Aux[A, Repr],
    decodeRepr: Decoder[Repr]
  ): Decoder[A] = decodeRepr.map(gen.from)

  def execute() = {
    decode[Expression]("""{ "x": "aaaaaa" }""") match {
      case Right(exp) => println(exp)
      case Left(_)    => println("Failure")
    }
    println((FooExpression(100): Expression).asJson.noSpaces)
  }
}
