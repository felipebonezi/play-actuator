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
name         := """play-actuator-test"""
organization := "io.github.felipebonezi"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

resolvers ++= DefaultOptions.resolvers(snapshot = true)
libraryDependencies += javaJdbc
libraryDependencies += cacheApi
libraryDependencies += guice
libraryDependencies += "org.postgresql"         % "postgresql" % "42.7.2"
libraryDependencies += "com.github.karelcemus" %% "play-redis" % "2.7.0"

lazy val actuatorVersion = "0.2.0+8-0577a9db+20221209-1830-SNAPSHOT"
libraryDependencies += "io.github.felipebonezi" %% "play-actuator"                 % actuatorVersion
libraryDependencies += "io.github.felipebonezi" %% "play-actuator-redis-indicator" % actuatorVersion
libraryDependencies += "io.github.felipebonezi" %% "play-actuator-jdbc-indicator"  % actuatorVersion

libraryDependencies += specs2                    % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.github.felipebonezi.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.github.felipebonezi.binders._"
