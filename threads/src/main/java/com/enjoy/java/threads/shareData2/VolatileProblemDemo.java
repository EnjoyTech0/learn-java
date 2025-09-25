package com.enjoy.java.threads.shareData2;

public class VolatileProblemDemo {
    // 没有 volatile 关键字的共享变量
    private static boolean flag = true;
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        // 读线程
        Thread reader = new Thread(() -> {
            System.out.println("Reader thread started...");
            while (flag) {
                count++;
                // 短循环，增加复现概率
            }
            System.out.println("Reader thread finished. Count: " + count);
        });

        // 写线程
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(2000); // 让读线程先运行
                flag = false;
                System.out.println("Writer thread set flag to false");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("Main thread finished");
    }
}