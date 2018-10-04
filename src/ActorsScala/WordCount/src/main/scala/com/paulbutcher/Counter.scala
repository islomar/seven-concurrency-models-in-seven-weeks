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

class Counter extends Actor {

  val counts = HashMap[String, Int]().withDefaultValue(0)

  def receive = {
    case Page(title, text) =>
      for (word <- Words(text))
        counts(word) += 1
      sender ! Processed
  }
}
