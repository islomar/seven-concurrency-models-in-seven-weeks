/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.concurrent.BlockingQueue;
import java.util.Map;

class Counter implements Runnable {

  private BlockingQueue<Page> queue;
  private Map<String, Integer> counts;

  public Counter(BlockingQueue<Page> queue,
                 Map<String, Integer> counts) {
    this.queue = queue;
    this.counts = counts;
  }

  public void run() {
    try {
      while(true) {
        Page page = queue.take();
        if (page.isPoisonPill())
          break;
        Iterable<String> words = new Words(page.getText());
        for (String word: words)
          countWord(word);
      }
    } catch (Exception e) { e.printStackTrace(); }
  }

  private void countWord(String word) {
    Integer currentCount = counts.get(word);
    if (currentCount == null)
      counts.put(word, 1);
    else
      counts.put(word, currentCount + 1);
  }
}
