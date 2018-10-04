/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher

import akka.actor._

case class Greet(name: String)
case class Praise(name: String)
case class Celebrate(name: String, age: Int)

class Master extends Actor {

  val talker = context.actorOf(Props[Talker], "talker")

  override def preStart {
    context.watch(talker)

    talker ! Greet("Huey")
    talker ! Praise("Dewey")
    talker ! Celebrate("Louie", 16)
    talker ! PoisonPill
  }

  def receive = {
    case Terminated(`talker`) => context.system.shutdown
  }
}

class Talker extends Actor {

  def receive = {
    case Greet(name) => println(s"Hello $name")
    case Praise(name) => println(s"$name, you're amazing")
    case Celebrate(name, age) => println(s"Here's to another $age years, $name")
  }
}

object HelloActors extends App {

  val system = ActorSystem("HelloActors")

  system.actorOf(Props[Master], "master")
}
