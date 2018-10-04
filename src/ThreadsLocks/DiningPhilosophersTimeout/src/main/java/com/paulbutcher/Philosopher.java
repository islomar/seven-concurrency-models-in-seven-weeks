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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class Philosopher extends Thread {
  private ReentrantLock leftChopstick, rightChopstick;
  private Random random;
  private int thinkCount;

  public Philosopher(ReentrantLock leftChopstick, ReentrantLock rightChopstick) {
    this.leftChopstick = leftChopstick; this.rightChopstick = rightChopstick;
    random = new Random();
  }

  public void run() {
    try {
      while(true) {
        ++thinkCount;
        if (thinkCount % 10 == 0)
          System.out.println("Philosopher " + this + " has thought " + thinkCount + " times");
        Thread.sleep(random.nextInt(1000)); // Think for a while
        leftChopstick.lock();
        try {
          if (rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
            // Got the right chopstick
            try {
              Thread.sleep(random.nextInt(1000)); // Eat for a while
            } finally { rightChopstick.unlock(); }
          } else {
            // Didn't get the right chopstick - give up and go back to thinking
            System.out.println("Philosopher " + this + " timed out");
          }
        } finally { leftChopstick.unlock(); }
      }
    } catch(InterruptedException e) {}
  }
}
