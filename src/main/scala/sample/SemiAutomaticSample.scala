package sample

import io.circe._, io.circe.generic.semiauto._, io.circe.syntax._

object SemiAutomaticSample {
  case class Foo(a: Int, b: String, c: Boolean)

  implicit val fooDecoder: Decoder[Foo] = deriveDecoder
  implicit val fooEncoder: Encoder[Foo] = deriveEncoder

  val execute: () => Unit = () => {

    val foo = Foo(100, "aaa", false)
    val fooJson = foo.asJson
    println(fooJson)
    println(fooJson.as[Foo])
  }
}
