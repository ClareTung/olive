package com.olive.java.start.multithread;

/**
 * 创建线程
 */
public class CreateThread {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        myThread.start();
        Thread.sleep(200);

        // 中断线程的睡眠
        myThread.interrupt();

        Thread thread = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " is run.");
        });
        thread.setName("second thread");
        thread.start();
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("MyThread is run.");
        }
    }
}
