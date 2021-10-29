package com.olive.java.start.threadalternateprinting;

import java.util.concurrent.Semaphore;

/**
 * 使用信号量
 *
 * Semaphore可以控制某个资源可被同时访问的个数，通过acquire()获取一个许可，如果没有就等待，而release()释放一个许可。
 */
public class UseSemaphore {

    // 共享资源个数初始都为1
    private static Semaphore s1 = new Semaphore(1);
    private static Semaphore s2 = new Semaphore(1);
    private static Semaphore s3 = new Semaphore(1);

    Thread t1 = new Thread(() -> {
        while(true) {
            try{
                s1.acquire();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("A");
            s2.release();
        }
    });

    Thread t2 = new Thread(() -> {
        while(true) {
            try{
                s2.acquire();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("B");
            s3.release();
        }
    });

    Thread t3 = new Thread(() -> {
        while(true) {
            try{
                s3.acquire();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("C");
            s1.release();
        }
    });

    public void fun() throws InterruptedException {
        // 先占有输出BC的线程的信号量计数
        // 则只能从A的线程开始，获取信号量A，然后释放B，获取B，释放C，获取C，释放A，由此形成循环
        s2.acquire();
        s3.acquire();
        t2.start();
        t3.start();
        t1.start();
    }

    public static void main(String[] args) throws InterruptedException {
        UseSemaphore us = new UseSemaphore();
        long t1 = System.currentTimeMillis();
        us.fun();
        while (true){
            if(System.currentTimeMillis() - t1 >= 10){
                System.exit(0);
            }
        }

    }


}
