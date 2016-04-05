name := "auth-service-client"
organization := "io.relayr"
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.5.1",
  "com.typesafe.play" %% "play-ws" % "2.5.1",
  "org.scalaz" %% "scalaz-core" % "7.2.2"
)