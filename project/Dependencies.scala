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
import sbt.Keys.libraryDependencies
import sbt._

// Single source of truth for the Play / Scala / ecosystem-library axis selected
// at sbt boot time. Pick the Play 3.0 axis with `-Dplay.version=3.0` or
// `PLAY_VERSION=3.0` in the env. Default stays on Play 2.9.
object PlayCrossBuilding {
  val DefaultPlayVersion = "2.9"

  val playMajor: String = sys.props
    .getOrElse("play.version", sys.env.getOrElse("PLAY_VERSION", DefaultPlayVersion))
    .trim

  val isPlay30: Boolean = playMajor.startsWith("3.")
  val isPlay29: Boolean = playMajor.startsWith("2.9")
  require(isPlay29 || isPlay30, s"Unsupported play.version=$playMajor (expected 2.9 or 3.0)")

  val Scala213: String            = "2.13.18"
  val Scala3: String              = "3.3.6"
  val crossScala: Seq[String]     = if (isPlay30) Seq(Scala213, Scala3) else Seq(Scala213)
  val defaultScalaVersion: String = Scala213

  val playGroup: String             = if (isPlay30) "org.playframework" else "com.typesafe.play"
  val playRedisGroup: String        = "com.github.karelcemus"
  val playVersion: String           = if (isPlay30) "3.0.10" else "2.9.10"
  val playJsonVersion: String       = if (isPlay30) "3.0.6" else "2.10.6"
  val playSlickVersion: String      = if (isPlay30) "6.2.0" else "5.3.0"
  val playRedisVersion: String      = if (isPlay30) "5.4.0" else "3.0.0"
  val typesafeConfigVersion: String = "1.4.3"

  // Suffix lets the two axes coexist in Maven Central without colliding.
  val artifactSuffix: String = if (isPlay30) "_play30" else "_play29"
}

object Dependencies {

  import PlayCrossBuilding._

  // Back-compat aliases preserved so the existing build.sbt structure
  // and any external references still resolve.
  val scala213: String           = Scala213
  val crossScalaSeq: Seq[String] = crossScala

  val core: Seq[ModuleID] = Seq(
    playGroup     %% "play-guice" % playVersion,
    playGroup     %% "play-test"  % playVersion % Test,
    "com.typesafe" % "config"     % typesafeConfigVersion
  )

  val actuator = libraryDependencies ++= core ++ Seq(
    playGroup %% "play-json" % playJsonVersion
  )

  val jdbc = libraryDependencies ++= core ++ Seq(
    playGroup %% "play-jdbc" % playVersion
  )

  val slick = libraryDependencies ++= core ++ Seq(
    playGroup %% "play-slick" % playSlickVersion
  )

  val redis = libraryDependencies ++= core ++ Seq(
    playRedisGroup %% "play-redis" % playRedisVersion
  )

}
