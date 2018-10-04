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
import collection.mutable.HashMap

case class Put(key: String, value: String)
case class Get(key: String)
case object ReportSize

class BuggyCache extends Actor {

  val cache = HashMap[String, String]()
  var size = 0

  def receive = {
    case Put(key, value) =>
      cache(key) = value
      size += value.length

    case Get(key) => sender ! Result(cache(key))

    case ReportSize => sender ! Result(size)
  }

  override def postRestart(reason: Throwable) {
    println("BuggyCache has restarted")
  }
}
