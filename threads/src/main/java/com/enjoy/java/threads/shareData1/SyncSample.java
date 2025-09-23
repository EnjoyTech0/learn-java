package com.enjoy.java.threads.shareData1;

/**
 * 线程不安全的示例: 超卖
 */
public class SyncSample {
    private static final Object object = new Object();
    public static Integer ticket = 2000;

    public static void main(String[] args) {

        Thread t1 = new Thread(new Task());
        Thread t2 = new Thread(new Task());
        Thread t3 = new Thread(new Task());

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException ignored) {
        }
    }

    public static class Task implements Runnable {
        @Override
        public void run() {
            while (ticket > 0) {
                synchronized (object) {
                    if (ticket > 0) {
                        System.out.println(Thread.currentThread().getName() + ": 卖票，票号为：" + ticket);
                        ticket--;
                    }
                }
            }
        }
    }
}