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

object Dependencies {

  val scala212 = "2.12.17"
  val scala213 = "2.13.10"

  val playVersion: String           = "2.8.19"
  val playSlickVersion: String      = "5.1.0"
  val playJsonVersion: String       = "2.10.4"
  val playRedisVersion: String      = "2.7.0"
  val typesafeConfigVersion: String = "1.4.2"

  val core: Seq[ModuleID] = Seq(
    "com.typesafe.play" %% "play-guice" % playVersion,
    "com.typesafe.play" %% "play-test"  % playVersion % Test,
    "com.typesafe"       % "config"     % typesafeConfigVersion
  )

  val actuator = libraryDependencies ++= core ++ Seq(
    "com.typesafe.play" %% "play-json" % playJsonVersion
  )

  val jdbc = libraryDependencies ++= core ++ Seq(
    "com.typesafe.play" %% "play-jdbc" % playVersion
  )

  val slick = libraryDependencies ++= core ++ Seq(
    "com.typesafe.play" %% "play-slick" % playSlickVersion
  )

  val redis = libraryDependencies ++= core ++ Seq(
    "com.github.karelcemus" %% "play-redis" % playRedisVersion
  )

}
