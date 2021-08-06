package com.olive.java.start.concurrent.thread;

/**
 * @description: Thread多次start
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/6 10:47
 */
public class ThreadTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> System.out.println("测试Thread多次start"));
        thread.start();
        thread.start();
    }
}
