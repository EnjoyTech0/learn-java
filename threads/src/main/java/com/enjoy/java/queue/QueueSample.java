package com.enjoy.java.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueSample {
    public static void main(String[] args) {
        // queue FIFO
        Queue<Integer> linkedList = new LinkedList<>();

        linkedList.add(1); // 添加成功返回true，失败抛出异常
        linkedList.offer(2); // 添加成功返回true，失败返回false
        linkedList.offer(3); // 添加成功返回true，失败返回false

        Integer removed = linkedList.remove(); // 移除头部元素成功返回被移除的元素，失败抛出异常
        System.out.println(removed);

        Integer poll = linkedList.poll(); // 移除头部元素成功返回被移除的元素，失败返回null
        System.out.println(poll);

        Integer element = linkedList.element(); // 返回(不移除)队列头部的元素，失败抛出异常
        System.out.println(element);
        Integer peek = linkedList.peek(); // 返回(不移除)队列头部的元素，失败返回null
        System.out.println(peek);

        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();


        int[] count = new int[]{1000};

        System.out.println(count[0]);
    }
}