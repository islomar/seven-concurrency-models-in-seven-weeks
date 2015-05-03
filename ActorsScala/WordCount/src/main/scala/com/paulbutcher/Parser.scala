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

case object Processed

class Parser(counter: ActorRef) extends Actor { 

  val pages = Pages(100000, "enwiki.xml")

  override def preStart {
    for (page <- pages.take(10)) 
      counter ! page
  }

  def receive = {
    case Processed if pages.hasNext => counter ! pages.next 
    case _ => context.stop(self) 
  }
}
