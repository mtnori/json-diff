package sample

import cats.syntax.functor._
import io.circe.{Decoder, Encoder}, io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode

object ADTSample {

  // ADT
  sealed trait Expression
  case class FooExpression(value: Int) extends Expression
  case class BarExpression(value: String) extends Expression
  case class BazExpression(value: Double) extends Expression

  // GenericDerivation
  implicit val encodeExpression: Encoder[Expression] = Encoder.instance {
    case fooExpression @ FooExpression(_) => fooExpression.asJson
    case barExpression @ BarExpression(_) => barExpression.asJson
    case bazExpression @ BazExpression(_) => bazExpression.asJson
  }
  implicit val decodeExpression: Decoder[Expression] =
    List[Decoder[Expression]](
      Decoder[FooExpression].widen,
      Decoder[BarExpression].widen,
      Decoder[BazExpression].widen
    ).reduceLeft(_ or _)

  def execute() = {
    decode[Expression]("""{ "value": 100.05 }""") match {
      case Right(exp) => println(exp)
      case Left(_)    => println("Failure")
    }
    println((BazExpression(100.05): Expression).asJson.noSpaces)
  }

}
