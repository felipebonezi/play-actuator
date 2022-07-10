name         := """play-actuator-test"""
organization := "io.github.felipebonezi"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, PlayActuator)

scalaVersion := "2.13.8"

resolvers ++= DefaultOptions.resolvers(snapshot = true)
libraryDependencies += guice
libraryDependencies += "io.github.felipebonezi" %% "play-actuator"      % "0.1.4-SNAPSHOT"
libraryDependencies += specs2                    % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.github.felipebonezi.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.github.felipebonezi.binders._"
