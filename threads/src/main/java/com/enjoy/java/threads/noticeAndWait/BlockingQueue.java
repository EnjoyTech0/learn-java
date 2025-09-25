package com.enjoy.java.threads.noticeAndWait;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int limit = 10;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue blockingQueue = new BlockingQueue();

        Thread t1 = new Thread(() -> {
            while (true) {
                blockingQueue.put(1);
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                blockingQueue.take();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    public synchronized void put(Integer i) {
        if (queue.size() == limit) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        queue.add(i);
        System.out.println("添加了" + i);
        notifyAll();
    }

    public synchronized Integer take() {
        if (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Integer polled = queue.poll();
        System.out.println("取出了" + polled);
        notifyAll();
        return polled;
    }
}