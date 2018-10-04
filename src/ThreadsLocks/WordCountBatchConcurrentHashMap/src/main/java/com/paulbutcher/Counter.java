/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

class Counter implements Runnable {

  private BlockingQueue<Page> queue;
  private ConcurrentMap<String, Integer> counts;
  private HashMap<String, Integer> localCounts;

  public Counter(BlockingQueue<Page> queue,
                 ConcurrentMap<String, Integer> counts) {
    this.queue = queue;
    this.counts = counts;
    localCounts = new HashMap<String, Integer>();
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
      mergeCounts();
    } catch (Exception e) { e.printStackTrace(); }
  }

  private void countWord(String word) {
    Integer currentCount = localCounts.get(word);
    if (currentCount == null)
      localCounts.put(word, 1);
    else
      localCounts.put(word, currentCount + 1);
  }

  private void mergeCounts() {
    for (Map.Entry<String, Integer> e: localCounts.entrySet()) {
      String word = e.getKey();
      Integer count = e.getValue();
      while (true) {
        Integer currentCount = counts.get(word);
        if (currentCount == null) {
          if (counts.putIfAbsent(word, count) == null)
            break;
        } else if (counts.replace(word, currentCount, currentCount + count)) {
          break;
        }
      }
    }
  }
}