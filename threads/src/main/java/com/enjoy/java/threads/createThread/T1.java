package com.enjoy.java.threads.createThread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的方式
 * <p>
 * start 方法会调用 start0 native 方法创建线程，然后JVM会调用 run 方法
 */
public class T1 extends Thread {

    public static void main(String[] args) {
        // 方式1：继承 Thread 类
        T1 t1 = new T1();
        t1.start();

        // 方式2：实现 Runnable 接口
        Thread t2 = new Thread(() -> System.out.println("hello world"));
        t2.start();

        // 3. 实现 Callable 接口，创建 FutureTask 对象，然后创建 Thread 对象，然后调用 start 方法
        FutureTask<String> futureTask = new FutureTask<>(() -> "hello world");
        Thread t3 = new Thread(futureTask);
        t3.start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("hello world");
    }
}