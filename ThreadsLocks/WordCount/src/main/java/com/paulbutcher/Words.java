/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.text.BreakIterator;
import java.util.Iterator;

class Words implements Iterable<String> {

  private final String text;

  public Words(String text) {
    this.text = text;
  }

  private class WordIterator implements Iterator<String> {

    private BreakIterator wordBoundary;
    private int start;
    private int end;

    public WordIterator() {
      wordBoundary = BreakIterator.getWordInstance();
      wordBoundary.setText(text);
      start = wordBoundary.first();
      end = wordBoundary.next();
    }

    public boolean hasNext() { return end != BreakIterator.DONE; }

    public String next() {
      String s = text.substring(start, end);
      start = end;
      end = wordBoundary.next();
      return s;
    }

    public void remove() { throw new UnsupportedOperationException(); }
  }

  public Iterator<String> iterator() {
    return new WordIterator();
  }
}