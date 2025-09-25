package com.enjoy.java.threads.noticeAndWait;

public class Example1 {
    // 创建一个专门用于同步的对象
    private static final Object lock = new Object();
    private static Integer ticket = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Producer());
        Thread t2 = new Thread(new Consumer());

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (true) {
                    if (ticket > 0) {
                        try {
                            System.out.println("生产者等待");
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ticket += 10;
                    System.out.println("生产了10张票");
                    lock.notify();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (true) {
                    if (ticket == 0) {
                        System.out.println("没有票了, 通知生产者");
                        lock.notify();
                        try {
                            System.out.println("消费者等待");
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    while (ticket > 0) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        --ticket;
                        System.out.println("剩余" + ticket + "张票");

                    }
                }
            }
        }
    }
}