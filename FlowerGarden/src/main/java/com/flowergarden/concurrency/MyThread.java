package com.flowergarden.concurrency;

/**
 * @author Yevheniia Zubrych on 26.04.2018.
 */
public class MyThread extends Thread {
    static long counter = 0;
    private final static Object lock = new Object();
    private int maximum;

    public MyThread(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public void run() {
        while (counter < maximum) {
            increment();
        }
    }

    private void increment() {
        synchronized (lock) {
            counter++;
        }
    }
}
