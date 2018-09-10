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
import akka.routing.{Broadcast, RoundRobinRouter}

class Master extends Actor {

  val accumulator = context.actorOf(Props[Accumulator], "accumulator")
  val counters = context.actorOf(
    Props(new Counter(accumulator)).withRouter(RoundRobinRouter(4)),
    "counter")
  val parser = context.actorOf(Props(new Parser(counters)), "parser")

  override def preStart {
    context.watch(accumulator)
    context.watch(counters)
    context.watch(parser)
  }

  def receive = {
    case Terminated(`parser`) => counters ! Broadcast(PoisonPill)
    case Terminated(`counters`) => accumulator ! PoisonPill
    case Terminated(`accumulator`) => context.system.shutdown
  }
}
