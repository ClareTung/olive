package com.olive.java.start.threadorderexecute;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用单线程线程池来实现
 */
public class SingleThreadExecutor {

    private static void printStr(String name) {
        System.out.println(name);
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> printStr("A"));
        Thread thread2 = new Thread(() -> printStr("B"));
        Thread thread3 = new Thread(() -> printStr("C"));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(thread1);
        executor.submit(thread2);
        executor.submit(thread3);

        executor.shutdown();
    }
}
