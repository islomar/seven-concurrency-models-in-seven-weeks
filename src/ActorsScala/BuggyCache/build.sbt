organization := "com.paulbutcher"

name := "actors-buggy-cache"

version := "1.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"

initialCommands in console := """
    import akka.actor._
    import com.paulbutcher._
    val system = ActorSystem("BuggyCache")
  """