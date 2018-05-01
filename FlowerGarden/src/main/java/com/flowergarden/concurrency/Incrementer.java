package com.flowergarden.concurrency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Yevheniia Zubrych on 25.04.2018.
 */
public class Incrementer {
    private static final int MAX_ITERATION = 1_000_000;
    private static final int THREAD_NUMB = 4;


    public void case1() {
        System.out.println("Case 1 started");
        long startTime = System.nanoTime();
        long i = 0L;
        while (i!=MAX_ITERATION) {
            i++;
        }
        System.out.println("Single code: " + i+"\t Time:"+((System.nanoTime() - startTime) / 1000/ 1000)+"ms");
        System.out.println("Case 1 finished\n");
    }

    public void case2() {
        System.out.println("Case 2 started");
        long startTime = System.nanoTime();
        Thread[] threads = new Thread[THREAD_NUMB];
        for (int j = 0; j < THREAD_NUMB; j++) {
            threads[j] = new Thread(new MyThread(MAX_ITERATION));
            threads[j].start();
        }

        for (int j = 0; j < THREAD_NUMB; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Extends thread: " + MyThread.counter+"\t Time:"+((System.nanoTime() - startTime) / 1000/ 1000)+"ms");
        System.out.println("Case 2 finished\n");
    }

    public void case3() {
        System.out.println("Case 3 started");
        long startTime = System.nanoTime();
        Runnable[] tasks = new Runnable[THREAD_NUMB];
        Thread[] threads = new Thread[THREAD_NUMB];
        AtomicCounter.reset();
        for (int j = 0; j < THREAD_NUMB; j++) {
            tasks[j] = () -> {
                while (AtomicCounter.get() < MAX_ITERATION) {
                    AtomicCounter.increment();
                }
            };
            threads[j] = new Thread(tasks[j]);
            threads[j].start();
        }
        for (int j = 0; j < THREAD_NUMB; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Runnable: " + AtomicCounter.get()+"\t Time:"+((System.nanoTime() - startTime) / 1000/ 1000)+"ms");
        System.out.println("Case 3 finished\n");
    }

    public void case4() {
        System.out.println("Case 4 started");
        long startTime = System.nanoTime();
        Counter.reset();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMB);
        Lock lock = new ReentrantLock();
        Collection<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_NUMB; i++) {
            tasks.add(() -> {
                lock.lock();
                try {
                    while (Counter.get() < MAX_ITERATION) {
                        Counter.increment();
                    }
                } finally {
                    lock.unlock();
                }
                return Counter.get();
            });
        }
        List<Future<Long>> futureList = null;
        try {
            futureList = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println("Cannot invoke all tasks. "+e.getMessage());
        }
        Float value=0f;
        assert futureList != null;
        for (Future<Long> future : futureList) {
            try {
                value += future.get()/THREAD_NUMB;
            } catch (InterruptedException|ExecutionException e) {
                System.err.println("Cannot get value from future: "+e.getMessage());
            }
        }
        executorService.shutdown();
        System.out.println("ReentrantLock: " + value+"\t Time:"+((System.nanoTime() - startTime) / 1000/ 1000)+"ms");
        System.out.println("Case 4 finished\n");
    }

    public void case5() {
        System.out.println("Case 5 started");
        long startTime = System.nanoTime();
        Counter.reset();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMB);
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Collection<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_NUMB; i++) {
            tasks.add(() -> {
                lock.writeLock().lock();
                try {
                    while (Counter.get() < MAX_ITERATION) {
                        Counter.increment();
                    }
                } finally {
                    lock.writeLock().unlock();
                }
                return Counter.get();
            });
        }
        List<Future<Long>> futureList = null;
        try {
            futureList = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println("Cannot invoke all tasks. "+e.getMessage());
        }
        Float value=0f;
        assert futureList != null;
        for (Future<Long> future : futureList) {
            try {
                lock.readLock().lock();
                value += future.get()/THREAD_NUMB;
                lock.readLock().unlock();
            } catch (InterruptedException|ExecutionException e) {
                System.err.println("Cannot get value from future: "+e.getMessage());
            }
        }
        executorService.shutdown();
        System.out.println("ReadWriteLock: " + value+"\t Time:"+((System.nanoTime() - startTime) / 1000/ 1000)+"ms");
        System.out.println("Case 5 finished\n");
    }

    public void case6() {
        System.out.println("Case 6 started");
        long startTime = System.nanoTime();
        AtomicLong counter = new AtomicLong(0);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMB);

        Collection<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_NUMB; i++) {
            tasks.add(() -> {
                    while (counter.get() < MAX_ITERATION) {
                        counter.incrementAndGet();
                    }
                return counter.get();
            });
        }
        List<Future<Long>> futureList = null;
        try {
            futureList = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println("Cannot invoke all tasks. "+e.getMessage());
        }
        Float value=0f;
        assert futureList != null;
        for (Future<Long> future : futureList) {
            try {
                value += future.get()/THREAD_NUMB;
            } catch (InterruptedException|ExecutionException e) {
                System.err.println("Cannot get value from future: "+e.getMessage());
            }
        }
        executorService.shutdown();
        System.out.println("AtomicLong: " + value+"\t Time:"+((System.nanoTime() - startTime) / 1000/ 1000)+"ms");
        System.out.println("Case 6 finished\n");
    }
}
