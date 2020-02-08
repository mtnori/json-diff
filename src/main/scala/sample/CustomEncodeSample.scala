package sample

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.syntax._

object CustomEncodeSample {

  class Thing(val foo: String, val bar: Int)

  implicit val encodeFoo: Encoder[Thing] = new Encoder[Thing] {
    final def apply(a: Thing): Json =
      Json.obj(("foo", Json.fromString(a.foo)), ("bar", Json.fromInt(a.bar)))
  }

  implicit val decodeFoo: Decoder[Thing] = new Decoder[Thing] {
    final def apply(c: HCursor): Decoder.Result[Thing] =
      for {
        foo <- c.downField("foo").as[String]
        bar <- c.downField("bar").as[Int]
      } yield {
        new Thing(foo, bar)
      }
  }

  def execute() = {
    val thing = new Thing("foo", 999)
    val thingJson = thing.asJson
    println(thingJson)
    println(thingJson.as[Thing])
  }

}
