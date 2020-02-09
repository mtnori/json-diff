package utils

/**
  * リソースであり、closeメソッドを実装していることを示す
  */
trait Resource {
  def close(): Unit
}

/**
  * try-with-resourceの代替となるローンパターン
  */
object Using {

  /**
    * try-with-resourceの代替となるローンパターン
    * クラスメンバ内のリソースを解放したいときに使う
    * @param r リソース
    * @param f 処理関数
    * @tparam A 処理関数の戻り値の型
    * @tparam R リソースの型(Resourceを継承していること)
    * @return A 処理関数の戻り値
    */
  def usingResource[A, R <: Resource](r: R)(f: R => A): A =
    try {
      f(r)
    } finally {
      r.close()
    }

  /**
    * try-with-resourceの代替となるローンパターン
    * TODO Scala 2.13 以降は標準ライブラリとして存在するはずなので不要
    * @param r リソース
    * @param f 処理関数
    * @tparam A 処理関数の戻り値の型
    * @tparam R リソースの型(AutoCloseableを継承していること)
    * @return A 処理関数の戻り値
    */
  def usingAutoCloseable[A, R <: AutoCloseable](r: R)(f: R => A): A =
    try {
      f(r)
    } finally {
      r.close()
    }
}
