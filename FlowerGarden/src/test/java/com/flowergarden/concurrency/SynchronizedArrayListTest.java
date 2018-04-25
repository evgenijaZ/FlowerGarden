package com.flowergarden.concurrency;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Yevheniia Zubrych on 24.04.2018.
 */
public class SynchronizedArrayListTest {
    private SynchronizedArrayList <Integer> list = new SynchronizedArrayList <>();

    @Test
    public void testSize() throws InterruptedException {
        int threadCount = 100;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new TestThread("T" + i));
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        assertEquals(threadCount * 100, list.size());
    }

    class TestThread implements Runnable {
        String threadName;

        TestThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                list.add(i);
                Thread.yield();
            }
        }
    }


}