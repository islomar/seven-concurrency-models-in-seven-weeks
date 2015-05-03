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
import akka.actor.SupervisorStrategy._
import java.util.NoSuchElementException

case class Result(result: Any)

class Master extends Actor {

  val cache = context.actorOf(Props[BuggyCache], "cache")

  def receive = {
    case Put(key, value) => cache ! Put(key, value)
    case Get(key) => cache ! Get(key)
    case ReportSize => cache ! ReportSize
    case Result(result) => println(result)
  }

  override val supervisorStrategy = OneForOneStrategy() {
      case _: NoSuchElementException => Resume
      case _: NullPointerException => Restart
      case _ => Escalate
    }
}
