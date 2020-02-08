import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import sample.{Foo, Qux}

object Main extends App {
  val foo: Foo = Qux(13, Some(14.0))

  val json = foo.asJson.noSpaces
  println(json)

  val decodedFoo = decode[Foo](json)
  println(decodedFoo)
}
