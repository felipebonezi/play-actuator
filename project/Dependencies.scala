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

  object ScalaVersions {
    val scala212 = "2.12.16"
    val scala213 = "2.13.8"
  }

  object Versions {
    val play: String           = "2.8.16"
    val playJson: String       = "2.9.2"
    val typesafeConfig: String = "1.4.2"
    val scripted: String       = "1.6.2"
  }

  val actuator = libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json"  % Versions.playJson,
    "com.typesafe.play" %% "play-guice" % Versions.play,
    "com.typesafe.play" %% "play-test"  % Versions.play % Test,
  )

  val plugin = libraryDependencies ++= Seq(
    "com.typesafe"       % "config"     % Versions.typesafeConfig,
    "com.typesafe.play" %% "play-guice" % Versions.play,
    "com.typesafe.play" %% "play-test"  % Versions.play % Test
  )
}
