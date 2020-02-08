package sample

import cats.syntax.either._
import io.circe._, io.circe.parser._

object CursorSample {
  def traversing() = {

    val json: String = """
      {
        "id": "c730433b-082c-4984-9d66-855c243266f0",
        "name": "Foo",
        "counts": [1, 2, 3],
        "values": {
          "bar": true,
          "baz": 100.001,
          "qux": ["a", "b"]
        }
      }
    """

    val doc: Json = parse(json).getOrElse(Json.Null)

    val cursor: HCursor = doc.hcursor

    val baz: Decoder.Result[Double] =
      cursor.downField("values").downField("baz").as[Double]

    val baz2: Decoder.Result[Double] =
      cursor.downField("values").get[Double]("baz")

    val secondQux: Decoder.Result[String] =
      cursor.downField("values").downField("qux").downArray.as[String]

    println(baz)
    println(baz2)
    println(secondQux)

    val reversedNameCursor: ACursor =
      cursor.downField("name").withFocus(_.mapString(_.reverse))

    val reversedName: Option[Json] = reversedNameCursor.top

    println(reversedName)
  }
}
