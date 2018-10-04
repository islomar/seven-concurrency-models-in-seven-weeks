/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.levdemidov;

public class Philosopher extends Thread {

  private boolean eating;
  private Philosopher left;
  private Philosopher right;
  private int thinkCount;
  public Object lockObject;

  public Philosopher() {
    eating = false;
    lockObject = new Object();
  }

  public void setLeft(Philosopher left) {
    this.left = left;
  }

  public void setRight(Philosopher right) {
    this.right = right;
  }

  public void run() {
    while (true) {
      try {
        // eat
        eating = false;
        synchronized (left.lockObject) {
          left.lockObject.notify();
        }
        synchronized (right.lockObject) {
          right.lockObject.notify();
        }
        Thread.sleep(1000);

        ++thinkCount;
        if (thinkCount % 10 == 0)
          System.out.println("Philosopher " + this + " has thought " + thinkCount + " times");

        // think
        synchronized (lockObject) {
          while (left.eating || right.eating)
            lockObject.wait();
          eating = true;
        }
        Thread.sleep(1000);

      } catch (InterruptedException e) {
        System.out.println("Error: " + e.toString());
      }
    }
  }
}
