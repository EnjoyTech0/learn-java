package com.enjoy.java.threads.lifeCyle;

public class Example1 {

    private static T1 t1;

    public static void main(String[] args) {
        t1 = new T1();
        System.out.println("T1 state after create: " + t1.getState());
        t1.start();
        System.out.println("T1 state after start: " + t1.getState());
    }

    public static class T1 extends Thread {
        @Override
        public void run() {
            T2 t2 = new T2();
            System.out.println("T2 state after create: " + t2.getState());

            t2.start();
            System.out.println("T2 state after start: " + t2.getState());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("T2 state after sleep: " + t2.getState());

            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("T2 state after join: " + t2.getState());
        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("T1 state after join: " + t1.getState());
        }
    }
}