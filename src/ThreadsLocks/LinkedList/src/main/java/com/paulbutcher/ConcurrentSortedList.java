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

class ConcurrentSortedList {

  private class Node {
    int value;
    Node prev;
    Node next;
    ReentrantLock lock = new ReentrantLock();

    Node() {}

    Node(int value, Node prev, Node next) {
      this.value = value; this.prev = prev; this.next = next;
    }
  }

  private final Node head;
  private final Node tail;

  public ConcurrentSortedList() {
    head = new Node(); tail = new Node();
    head.next = tail; tail.prev = head;
  }

  public void insert(int value) {
    Node current = head;
    current.lock.lock(); 
    Node next = current.next;
    try {
      while (true) {
        next.lock.lock(); 
        try {
          if (next == tail || next.value < value) { 
            Node node = new Node(value, current, next); 
            next.prev = node;
            current.next = node;
            return; 
          }
        } finally { current.lock.unlock(); } 
        current = next;
        next = current.next;
      }
    } finally { next.lock.unlock(); } 
  }

  public int size() {
    Node current = tail;
    int count = 0;
    while (current.prev != head) {
      ReentrantLock lock = current.lock;
      lock.lock();
      try {
        ++count;
        current = current.prev;
      } finally { lock.unlock(); }
    }
    return count;
  }

  public boolean isSorted() {
    Node current = head;
    while (current.next.next != tail) {
      current = current.next;
      if (current.value < current.next.value)
        return false;
    }
    return true;
  }
}
