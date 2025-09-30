package com.enjoy.java.aqs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class AQSTest {
    private static final int[] count = new int[]{10};

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();

        // FakeLock lock = new FakeLock();
        ReentrantLock lock = new ReentrantLock();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                lock.lock();
                try {
                    Thread.sleep(2);
                    count[0]--;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }));
        }

        threads.forEach(Thread::start);

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(count[0]);
    }

}