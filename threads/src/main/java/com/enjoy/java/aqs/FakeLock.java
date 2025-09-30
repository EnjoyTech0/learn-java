package com.enjoy.java.aqs;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class FakeLock {

    AtomicBoolean flag = new AtomicBoolean(false);

    Thread owner;

    AtomicReference<Node> head = new AtomicReference<>(new Node());

    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    public void lock() {
        if (flag.compareAndSet(false, true)) {
            System.out.println(Thread.currentThread().getName() + "直接获得锁");
            owner = Thread.currentThread();
            return;
        }

        Node current = new Node();
        current.thread = Thread.currentThread();

        // 确保tail更新成功
        while (true) {
            Node currentTail = tail.get();
            if (tail.compareAndSet(currentTail, current)) {
                currentTail.next = current;
                current.prev = currentTail;
                current.next = null;
                System.out.println(Thread.currentThread().getName() + "加入队列");
                break;
            }
        }

        while (true) {
            if (current.prev == head.get() && flag.compareAndSet(false, true)) {
                owner = Thread.currentThread();
                head.set(current);
                current.prev.next = null;
                current.prev.thread = null;
                current.prev = null;
                System.out.println(Thread.currentThread().getName() + "获得锁");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "等待锁");
            LockSupport.park();
        }
    }

    public void unlock() {
        if (Thread.currentThread() != owner) {
            throw new IllegalStateException("不是锁的拥有者");
        }

        // System.out.println(Thread.currentThread().getName() + "释放锁");
        Node head = this.head.get();
        Node next = head.next;
        flag.set(false);
        if (next != null) {
            System.out.println(Thread.currentThread().getName() + "唤醒下一个线程" + next.thread.getName());
            LockSupport.unpark(next.thread);
        }
    }

    private static class Node {
        Thread thread;
        Node next;
        Node prev;
    }
}