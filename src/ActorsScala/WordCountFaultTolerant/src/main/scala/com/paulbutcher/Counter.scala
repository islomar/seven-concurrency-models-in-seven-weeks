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

case class ParserAvailable(parser: ActorRef)
case class Batch(id: Int, pages: Seq[Page], accumulator: ActorRef)

class Counter extends Actor {

  def receive = {
    case ParserAvailable(parser) => parser ! RequestBatch

    case Batch(id, pages, accumulator) =>
      sender ! RequestBatch
      val counts = HashMap[String, Int]().withDefaultValue(0)
      for (page <- pages)
        for (word <- Words(page.text))
          counts(word) += 1
      accumulator ! Counts(id, counts)
  }
}
