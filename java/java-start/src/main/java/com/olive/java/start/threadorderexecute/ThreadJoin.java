package com.olive.java.start.threadorderexecute;

/**
 * Thread.join()保证线程顺序执行
 */
public class ThreadJoin {

    private static void printStr(String name) {
        System.out.println(name);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> printStr("A"));
        Thread thread2 = new Thread(() -> printStr("B"));
        Thread thread3 = new Thread(() -> printStr("C"));

        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        thread3.start();
    }
}
