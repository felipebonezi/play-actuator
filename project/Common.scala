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
import Dependencies.ScalaVersions._
import de.heikoseeberger.sbtheader.HeaderPlugin
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object Common extends AutoPlugin {

  import HeaderPlugin.autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin && HeaderPlugin

  val repoName = "play-actuator"

  override def globalSettings: Seq[Setting[_]] =
    Seq(
      // project
      description := "A Play! Framework dependency to include actuators methods to your service.",
      // organization
      organization         := "io.github.felipebonezi",
      organizationName     := "Felipe Bonezi",
      organizationHomepage := Some(url("https://about.me/felipebonezi")),
      // scala settings
      scalaVersion := scala212,
      scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-encoding", "utf8"),
      javacOptions ++= Seq("-encoding", "UTF-8"),
      // legal
      licenses := Seq(
        "MIT" ->
          url("https://github.com/felipebonezi/play-actuator/blob/main/LICENSE")
      ),
      // on the web
      homepage := Some(url(s"https://github.com/felipebonezi/$repoName")),
      scmInfo := Some(
        ScmInfo(
          url(s"https://github.com/felipebonezi/$repoName"),
          s"scm:git:git@github.com:felipebonezi/$repoName.git"
        )
      ),
      developers += Developer(
        "contributors",
        "Contributors",
        s"https://github.com/felipebonezi/$repoName/graphs/contributors",
        url("https://github.com/felipebonezi")
      ),
      versionScheme     := Some("early-semver"),
      publishMavenStyle := true,
      credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
    )

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      headerEmptyLine := false,
      headerLicense :=
        Some(HeaderLicense.MIT("2022", "Felipe Bonezi <https://about.me/felipebonezi>"))
    )
}
