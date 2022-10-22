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
addSbtPlugin("de.heikoseeberger" % "sbt-header"     % "5.7.0")
addSbtPlugin("com.github.sbt"    % "sbt-ci-release" % "1.5.10")

// Code formatter for Scala.
// See more: https://github.com/scalameta/sbt-scalafmt
// Oficial Website: https://scalameta.org/scalafmt/
addSbtPlugin("org.scalameta"   % "sbt-scalafmt"          % "2.4.6")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// Test Coverage plugin.
// sbt-scoverage is a plugin for SBT that integrates the scoverage code coverage library.
// See more: https://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.6")

// Fixing Scala-Xml mess (Temp).
ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
