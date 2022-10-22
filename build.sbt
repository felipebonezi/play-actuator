/*
 * Copyright (c) 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
import Common.repoName
import Dependencies.scala212
import Dependencies.scala213

lazy val root = project
  .in(file("."))
  .aggregate(actuator)
  .settings(
    name               := "play-actuator-root",
    crossScalaVersions := Nil,
    publish / skip     := true,
  )

lazy val core = project
  .in(file("play-actuator-core"))
  .settings(
    scalaVersion       := scala213,
    crossScalaVersions := Seq(scala212, scala213),
    libraryDependencies ++= Dependencies.core,
    publish / skip := true
  )

lazy val actuator = project
  .in(file("play-actuator"))
  .dependsOn(core)
  .settings(
    name                               := s"$repoName",
    organization                       := "io.github.felipebonezi",
    scalaVersion                       := scala213,
    crossScalaVersions                 := Seq(scala212, scala213),
    versionScheme                      := Some("early-semver"),
    ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org",
    ThisBuild / sonatypeRepository     := "https://s01.oss.sonatype.org/service/local",
    scmInfo := Some(
      ScmInfo(
        url(s"https://github.com/felipebonezi/$repoName"),
        s"scm:git:git@github.com:felipebonezi/$repoName.git"
      )
    ),
    Dependencies.actuator,
  )
  .enablePlugins(Common)

lazy val database = project
  .in(file("play-actuator-indicators/database"))
  .dependsOn(core)
  .settings(
    scalaVersion       := scala213,
    crossScalaVersions := Seq(scala212, scala213),
    Dependencies.db,
    publish / skip := true
  )

lazy val redis = project
  .in(file("play-actuator-indicators/redis"))
  .dependsOn(core)
  .settings(
    scalaVersion       := scala213,
    crossScalaVersions := Seq(scala212, scala213),
    Dependencies.redis,
    publish / skip := true
  )

addCommandAlias(
  "validateCode",
  List(
    "headerCheckAll",
    "scalafmtSbtCheck",
    "scalafmtCheckAll",
    "test:scalafmtCheckAll",
    "scalastyle",
    "test:scalastyle",
  ).mkString(";")
)
