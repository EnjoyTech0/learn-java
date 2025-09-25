package com.enjoy.java.threads.noticeAndWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 和 Condition 实现阻塞队列
 */
public class FakeBlockingQueue2<T> {

    private Integer capacity;
    private AtomicInteger size = new AtomicInteger(0);
    private ReentrantLock putLock = new ReentrantLock();
    private Condition notFull = putLock.newCondition();
    private ReentrantLock takeLock = new ReentrantLock();
    private Condition notEmpty = takeLock.newCondition();
    private List<T> list = new ArrayList<>();

    public FakeBlockingQueue2(Integer capacity) {
        this.capacity = capacity;
    }

    public static void main(String[] args) throws InterruptedException {
        FakeBlockingQueue2<Integer> queue = new FakeBlockingQueue2<>(10);

        Thread p1 = new Thread(new Producer(queue));
        Thread p2 = new Thread(new Producer(queue));
        Thread c1 = new Thread(new Consumer(queue));
        Thread c2 = new Thread(new Consumer(queue));
        Thread c3 = new Thread(new Consumer(queue));

        p1.start();
        // p2.start();
        c1.start();
        c2.start();
        c3.start();

        p1.join();
        // p2.join();
        c1.join();
        c2.join();
        c3.join();
    }

    public void put(T t) throws InterruptedException {
        putLock.lock();
        while (size.get() >= capacity) {
            System.out.println("队列已满");
            notFull.await();
        }
        try {
            list.add(t);
            int i = size.incrementAndGet();
            if (i > 0) {
                // notFull.signal();
                signalNotEmpty();
            }
        } finally {
            putLock.unlock();
        }
    }

    public T take() throws InterruptedException {
        takeLock.lock();
        T removed;
        while (size.get() == 0) {
            System.out.println("队列已空");
            notEmpty.await();
        }

        try {
            removed = list.remove(0);
            int i = size.getAndDecrement();
            if (i > 1) {
                // notEmpty.signal();
                signalNotFull();
            }
            // notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        return removed;
    }

    private void signalNotFull() {
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    private void signalNotEmpty() {
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }


    public static class Producer implements Runnable {
        private FakeBlockingQueue2<Integer> queue;

        public Producer(FakeBlockingQueue2<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(20);
                    queue.put(1);
                    System.out.println(Thread.currentThread() + " producer");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer implements Runnable {
        private FakeBlockingQueue2<Integer> queue;

        public Consumer(FakeBlockingQueue2<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Consumer " + Thread.currentThread() + " " + queue.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}