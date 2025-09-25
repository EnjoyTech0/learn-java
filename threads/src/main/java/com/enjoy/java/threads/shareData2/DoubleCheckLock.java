package com.enjoy.java.threads.shareData2;

public class DoubleCheckLock {
    public static volatile DoubleCheckLock instance;

    private DoubleCheckLock() {
    }

    public static DoubleCheckLock getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLock.class) {
                if (instance == null) {
                    // 对 volatile 修饰的 instance 进行初始化（写）操作，
                    // volatile 保证了写操作前后添加内存屏障，
                    // 保证 instance = new DLC() （分配内存、引用指向内存、初始化对象三个步骤）全部完成后其他线程才能读取到instance对象
                    instance = new DoubleCheckLock();
                }
            }
        }
        return instance;
    }
}