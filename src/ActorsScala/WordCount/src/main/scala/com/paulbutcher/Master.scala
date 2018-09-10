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

class Master extends Actor {

  val counter = context.actorOf(Props[Counter], "counter")
  val parser = context.actorOf(Props(new Parser(counter)), "parser")

  override def preStart {
    context.watch(counter)
    context.watch(parser)
  }

  def receive = {
    case Terminated(`parser`) => counter ! PoisonPill
    case Terminated(`counter`) => context.system.shutdown
  }
}
