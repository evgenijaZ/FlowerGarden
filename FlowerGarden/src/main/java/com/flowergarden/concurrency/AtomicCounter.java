package com.flowergarden.concurrency;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter {
    private static AtomicLong i = new AtomicLong(0);

    static long get(){
        return i.get();
    }

    static void increment(){
        i.incrementAndGet();
    }

    static void reset(){
        i.set(0);
    }
}
