package filesystem

import java.io.{BufferedReader, IOException, UncheckedIOException}
import java.nio.charset.StandardCharsets
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileSystem, FileSystems, Files, Path, Paths}
import java.util.Scanner
import java.util.function.BiPredicate

import scala.collection.mutable.ArrayBuffer

object FileSystemSample {

  val zipMatcher: BiPredicate[Path, BasicFileAttributes] = (path, attr) => {
    if (attr.isRegularFile && path.getFileName.toString.endsWith(".zip")) true
    else false
  }

  val jsonMatcher: BiPredicate[Path, BasicFileAttributes] = (path, attr) => {
    if (attr.isRegularFile && path.getFileName.toString.endsWith(".txt")) true
    else false
  }

  /**
    * フォルダからZipファイルの中身のJsonの文字を返す
    * TODO 最終的にList[string]で返す。もしくはタプル
    */
  val getZipInnerJsonFilePaths: String => String => String =
    (baseDir: String) =>
      (subDir: String) => {
        val path = Paths.get(baseDir).resolve(subDir)

        println(path.toAbsolutePath)

        val zipFiles = new ArrayBuffer[Path]
        Files.find(path, 1, zipMatcher).forEach { zipFile =>
          {
            zipFiles += zipFile
          }
        }

        zipFiles.headOption match {
          case Some(path: Path) => {
            getJsonFilePaths(path)
          }
          case _ => throw new Exception("zip file is not found")
        }
    }

  /**
    * ZipファイルのPathから、Zipファイル内の
    * Jsonファイルの内容を取得する
    * TODO こっちで返し方かえるかも
    */
  val getJsonFilePaths: Path => String =
    (path: Path) => {
      val fs =
        FileSystems.newFileSystem(path, null)
      val root = fs.getPath("/")

      val sb = new StringBuilder
      Files.find(root, 2, jsonMatcher).forEach { innerPath =>
        {
          sb.append(readJsonFile(innerPath))
        }
      }
      sb.toString()
    }

  // try-with-resourceの代替となるローンパターン
  def using[R <: AutoCloseable, A](resource: R)(f: R => A): A =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  // JSONファイルの読み込み
  val readJsonFile: Path => String = (path: Path) => {
    val sb = new StringBuilder
    using(Files.newBufferedReader(path, StandardCharsets.UTF_8)) { in =>
      {
        using(new Scanner(in)) { sc =>
          {
            //sc.useDelimiter("(,|\\n)")
            while (sc.hasNext()) {
              sb.append(sc.next())
            }
            sb.toString()
          }
        }
      }
    }
  }
}
