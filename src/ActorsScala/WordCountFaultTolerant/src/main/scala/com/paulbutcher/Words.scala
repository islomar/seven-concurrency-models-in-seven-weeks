/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher

import java.text.BreakIterator

class Words(text: String) extends Iterator[String] {
  val wordBoundary = BreakIterator.getWordInstance
  wordBoundary.setText(text)
  var start = wordBoundary.first
  var end = wordBoundary.next
  
  def hasNext = end != BreakIterator.DONE
  
  def next() = {
    val s = text.subSequence(start, end)
    start = end
    end = wordBoundary.next
    s.toString
  }
}

object Words {
  def apply(text:String) = new Words(text)
}