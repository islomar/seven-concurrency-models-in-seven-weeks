package com.islomar;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class CounterJava9 {

    // Invariant: value >= 0
    private volatile int value;
    private final static VarHandle VALUE; //you use in capital letters the name of the variable that you want to protect
    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(
                    CounterJava9.class, "value", int.class
            );
        } catch(ReflectiveOperationException e) {
            throw new Error(e);
        }

    }

    public CounterJava9(int value) {
        if (value < 0)
            throw new ArithmeticException("overflow");
        this.value = value;
    }

    public int incrementAndGet() {
        return getAndIncrement() + 1;
    }

    public int getAndIncrement() {
        int current;
        do {
            current = value;
            if (current == Integer.MAX_VALUE) {
                new ArithmeticException("overflow");
            }
        } while (!VALUE.compareAndSet(this, current, current + 1));
        return current;
    }

    public boolean isSafeIncrement() {
        int current;
        do {
            current = value;
            if (current == Integer.MAX_VALUE) {
                return false;
            }
        } while (!VALUE.compareAndSet(this, current, current + 1));
        return true;
    }

    public int get() {
        return value;
    }
}
