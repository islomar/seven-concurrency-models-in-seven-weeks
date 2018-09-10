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
import collection.mutable.{LinkedHashMap, Queue}

case object RequestBatch
case class Processed(id: Int)

class Parser(filename: String, batchSize: Int, limit: Int) extends Actor {

  val pages = Pages(limit, filename)
  var nextId = 1
  val pending = LinkedHashMap[Int, Batch]() 

  val accumulator = context.actorOf(Props(new Accumulator(self))) 

  def receive = {
    case RequestBatch =>
      if (pages.hasNext) {
        val batch = Batch(nextId, pages.take(batchSize).toVector, accumulator) 
        pending(nextId) = batch
        sender ! batch
        nextId += 1
      } else {
        val (id, batch) = pending.head  // The oldest pending item 
        pending -= id                   // Remove and re-add so it's now
        pending(id) = batch             // the youngest
        sender ! batch
      }

    case Processed(id) =>
      pending.remove(id)
      if (!pages.hasNext && pending.isEmpty)
        context.system.shutdown
  }
}
