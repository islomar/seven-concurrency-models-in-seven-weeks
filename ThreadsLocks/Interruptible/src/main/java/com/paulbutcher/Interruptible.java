/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.concurrent.locks.ReentrantLock;

public class Interruptible {

  public static void main(String[] args) throws InterruptedException {

    final ReentrantLock l1 = new ReentrantLock();
    final ReentrantLock l2 = new ReentrantLock();

    Thread t1 = new Thread() {
      public void run() {
        try {
          l1.lockInterruptibly();
          try {
            Thread.sleep(1000);
            l2.lockInterruptibly();
            l2.unlock();
          } finally {
            l1.unlock();
          }
        } catch (InterruptedException e) { System.out.println("t1 interrupted"); }
      }
    };

    Thread t2 = new Thread() {
      public void run() {
        try {
          l2.lockInterruptibly();
          try {
            Thread.sleep(1000);
            l1.lockInterruptibly();
            l1.unlock();
          } finally {
            l2.unlock();
          }
        } catch (InterruptedException e) { System.out.println("t2 interrupted"); }
      }
    };

    t1.start(); t2.start();
    Thread.sleep(2000);
    t1.interrupt(); t2.interrupt();
    t1.join(); t2.join();
  }
}
