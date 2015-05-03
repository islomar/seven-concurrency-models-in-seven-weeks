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

case class CreateChild(name: String)
case class Divide(x: Int, y: Int)

class TestActor extends Actor {

  def receive = {
    case CreateChild(name) => context.actorOf(Props[TestActor], name)
    case Divide(x, y) => log(s"$x / $y = ${x / y}")
  }

  override def preStart() { log(s"preStart") }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    log(s"preRestart ($reason, $message)")
  }

  override def postRestart(reason: Throwable) { log(s"postRestart ($reason)") }

  override def postStop() { log(s"postStop") }

  def log(message: String) { println(s"${self.path.name}: $message") }
}
