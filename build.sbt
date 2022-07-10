import Dependencies.ScalaVersions._

import sbt.DefaultOptions
import sbt.Keys.resolvers

lazy val root = project
  .in(file("."))
  .aggregate(core)
  .settings(
    name               := "play-actuator-root",
    crossScalaVersions := Nil,
    publish / skip     := true,
  )

lazy val core = project
  .in(file("play-actuator"))
  .settings(
    name               := "play-actuator",
    organization       := "io.github.felipebonezi",
    version            := "0.1.0",
    crossScalaVersions := Seq(scala212, scala213),
    Dependencies.actuator,
  )

addCommandAlias(
  "validateCode",
  List(
    "headerCheckAll",
    "scalafmtSbtCheck",
    "scalafmtCheckAll",
    "scalastyle",
  ).mkString(";")
)
