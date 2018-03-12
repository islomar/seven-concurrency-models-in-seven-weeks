package com.islomar;

// The counter is thread-safe
public class Counter {

    // Invariant: value >= 0
    private int value;

    public Counter(int value) {
        if (value < 0)
            throw new ArithmeticException("overflow");
        this.value = value;
    }

    public synchronized int incrementAndGet() {
        if (value == Integer.MAX_VALUE)
            throw new ArithmeticException("overflow");
        return ++value;
    }

    public synchronized int getAndIncrement() {
        if (value == Integer.MAX_VALUE)
            throw new ArithmeticException("overflow");
        return value++;
    }

    public synchronized int get() {
        return value;
    }
}
