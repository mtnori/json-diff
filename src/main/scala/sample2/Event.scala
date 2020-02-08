package sample2

sealed trait Event

case class Foo(value: Int) extends Event
case class Bar(value: String) extends Event
case class Baz(value: Double) extends Event
case class Qux(values: List[String]) extends Event
