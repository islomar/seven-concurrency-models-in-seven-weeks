organization := "com.paulbutcher"

name := "actors-word-count-flow-control"

version := "1.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"
