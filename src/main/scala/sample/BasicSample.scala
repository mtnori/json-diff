package sample

import io.circe._, io.circe.parser._

sealed trait Foo
case class Bar(xs: Vector[String]) extends Foo
case class Qux(i: Int, d: Option[Double]) extends Foo
object Sample {

  val successJson: String = """
    {
      "foo": "bar",
      "baz": 123,
      "list of stuff": [ 4, 5, 6 ]
    }
   """

  val failureJson: String = "yolo"

  def parse1(rawJson: String) = parse(rawJson)

}
