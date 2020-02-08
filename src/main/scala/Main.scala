import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import sample.{Foo, Qux, Sample}

object Main extends App {
  val foo: Foo = Qux(13, Some(14.0))

  val json = foo.asJson.noSpaces
  println(json)

  val decodedFoo = decode[Foo](json)
  println(decodedFoo)

  println(Sample.parse1(Sample.successJson))

  Sample.parse1(Sample.failureJson) match {
    case Left(failure) => println("Invalid Json :(")
    case Right(json)   => println("Yay, got some JSON!")
  }
}
