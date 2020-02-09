package utils

trait Resource {
  def close()
}

// try-with-resourceの代替となるローンパターン
object Using {

  // Resourceトレイトを継承すること
  def usingResource[A, R <: Resource](r: R)(f: R => A): A =
    try {
      f(r)
    } finally {
      r.close()
    }

  def usingAutoCloseable[A, R <: AutoCloseable](r: R)(f: R => A): A =
    try {
      f(r)
    } finally {
      r.close()
    }
}
