package sample

import io.circe.syntax._

object EncodingSample {
  val execute: () => Unit = () => {
    // Encoding (A -> JSON)
    val intsJson = List(1, 2, 3).asJson
    println(intsJson)

    // Decoding (JSON -> A)
    val json = intsJson.as[List[Int]]
    println(json)
  }
}
