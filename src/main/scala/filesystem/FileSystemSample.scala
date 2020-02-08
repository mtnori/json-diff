package filesystem

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileSystem, FileSystems, Files, Path, Paths}
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
    * フォルダからZipファイルのPathを取得する
    */
  val getZipInnerJsonFilePaths: String => String => Seq[Path] =
    (baseDir: String) =>
      (subDir: String) => {
        val path = Paths.get(baseDir).resolve(subDir)

        println(path.toAbsolutePath)

        val zipFiles = new ArrayBuffer[Path]
        Files.find(path, 1, zipMatcher).forEach { x =>
          {
            zipFiles += x
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
    * JsonファイルのPathリストを取得する
    */
  val getJsonFilePaths: Path => Seq[Path] =
    (path: Path) => {
      val fs =
        FileSystems.newFileSystem(path, null)
      val root = fs.getPath("/")

      val jsonFiles = new ArrayBuffer[Path]
      Files.find(root, 2, jsonMatcher).forEach { x =>
        {
          jsonFiles += x
        }
      }
      jsonFiles.toSeq
    }
}
