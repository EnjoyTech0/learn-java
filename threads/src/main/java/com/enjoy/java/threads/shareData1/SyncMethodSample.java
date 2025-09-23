package com.enjoy.java.threads.shareData1;

/**
 * 线程不安全的示例: 超卖
 */
public class SyncMethodSample {
    public static Integer ticket = 1000;

    public static void main(String[] args) {
        new Task().run();
    }

    public static class Task {
        public void run() {

            Runnable task = new Runnable() {
                @Override
                public void run() {
                    while (ticket > 0) {
                        sell();
                    }
                }
            };

            Thread t1 = new Thread(task);
            Thread t2 = new Thread(task);
            Thread t3 = new Thread(task);

            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException ignored) {
            }

            while (ticket > 0) {
                sell();
            }
        }

        public synchronized void sell() {
            if (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + ": 卖票，票号为：" + ticket);
                ticket--;
            }
        }
    }
}