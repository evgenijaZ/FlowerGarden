package com.flowergarden.concurrency;

public class Counter {
    private static long i = 0;

    static long get() {
        return i;
    }

    static void increment() {
        i++;
    }

    static void reset() {
        i = 0;
    }
}
