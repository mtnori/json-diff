package sample2

import io.circe.{Decoder, Encoder}

/**
  * 抽象型に対するEncode/Decode設定
  */
object ShapesDerivation {
  type EventMap = Map[String, Event]
  case class NameWithEventMap(name: String, events: EventMap)

  import shapeless.{Coproduct, Generic}

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

//  sealed trait Event
//
//  case class Foo(i: Int) extends Event
//  case class Bar(s: String) extends Event
//  case class Baz(c: Char) extends Event
//  case class Qux(values: List[String]) extends Event
}
