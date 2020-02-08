package sample

import io.circe.generic.auto._, io.circe.syntax._

object AutomaticSample {
  case class Person(name: String)
  case class Greeting(salutation: String, person: Person, exclamationMarks: Int)

  def execute(): Unit =
    println(Greeting("Hey", Person("Chris"), 3).asJson)
}
