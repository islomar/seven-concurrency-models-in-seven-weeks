/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.Random;

class LinkedList {

  public static void main(String[] args) throws InterruptedException {
    final ConcurrentSortedList list = new ConcurrentSortedList();
    final Random random = new Random();

    class TestThread extends Thread {
      public void run() {
        for (int i = 0; i < 10000; ++i)
          list.insert(random.nextInt());
      }
    }

    class CountingThread extends Thread {
      public void run() {
        while (!interrupted()) {
          System.out.print("\r" + list.size());
          System.out.flush();
        }
      }
    }

    Thread t1 = new TestThread();
    Thread t2 = new TestThread();
    Thread t3 = new CountingThread();

    t1.start(); t2.start(); t3.start();
    t1.join(); t2.join();
    t3.interrupt();

    System.out.println("\r" + list.size());

    if (list.size() != 20000)
      System.out.println("*** Wrong size!");

    if (!list.isSorted())
      System.out.println("*** Not sorted!");
  }
}