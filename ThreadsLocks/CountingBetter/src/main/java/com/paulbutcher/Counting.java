/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.concurrent.atomic.AtomicInteger;

public class Counting {

  public static void main(String[] args) throws InterruptedException {
    
    final AtomicInteger counter = new AtomicInteger();
    
    class CountingThread extends Thread {
      public void run() {
        for(int x = 0; x < 10000; ++x)
          counter.incrementAndGet();
      }
    }

    CountingThread t1 = new CountingThread();
    CountingThread t2 = new CountingThread();
    
    t1.start(); t2.start();
    t1.join(); t2.join();
    
    System.out.println(counter.get());
  }
}
