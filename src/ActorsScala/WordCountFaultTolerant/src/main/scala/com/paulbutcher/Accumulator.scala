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
import collection.Map
import collection.mutable.{HashMap, Set}

case class Counts(id: Int, counts: Map[String, Int])

class Accumulator(parser: ActorRef) extends Actor {
  
  val counts = HashMap[String, Int]().withDefaultValue(0)
  val processedIds = Set[Int]()

  def receive = {
    case Counts(id, partialCounts) =>
      if (!processedIds.contains(id)) {
        for ((word, count) <- partialCounts)
          counts(word) += count
        processedIds += id
        parser ! Processed(id)
      }
  }
}
