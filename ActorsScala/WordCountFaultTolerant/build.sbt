organization := "com.paulbutcher"

name := "actors-word-count-fault-tolerant"

version := "1.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0",
  "com.typesafe.akka" %% "akka-cluster-experimental" % "2.1.0",
  "com.github.scopt" %% "scopt" % "2.1.0")
