package com.islomar;

// This client is thread-safe
public class CounterClient {
    public static void main(String[] args) {
        Counter counter = new Counter(Integer.MAX_VALUE - 1);

        synchronized (counter) {
            if (counter.get() < Integer.MAX_VALUE) {
                System.out.println(counter.incrementAndGet());
            } else {
                System.err.println("That would overflow");
            }
        }
    }
}
