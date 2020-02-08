name := "json-diff"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.13.1",
  test in assembly := {}
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    assemblyJarName in assembly := "json-diff.jar",
    // more settings here ...
  )

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
