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
case object SayHello
case class SayHelloFrom(path: String)

class TestActor extends Actor {

  def receive = {
    case CreateChild(name) => context.actorOf(Props[TestActor], name)
    case SayHello => println(s"Hello from $self")
    case SayHelloFrom(path) => context.actorFor(path) ! SayHello
  }
}
