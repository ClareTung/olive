package com.olive.java.start.threadorderexecute;

/**
 * 使用volatile关键字修饰的信号量实现
 */
public class UseVolatile {
    //信号量
    static volatile int ticket = 1;
    //线程休眠时间
    public final static int SLEEP_TIME = 1;

    public static void foo(int name) {
        //因为线程的执行顺序是不可预期的，因此需要每个线程自旋
        while (true) {
            if (ticket == name) {
                try {
                    Thread.sleep(SLEEP_TIME);
                    //每个线程循环打印3次
                    for (int i = 0; i < 3; i++) {
                        System.out.println(Thread.currentThread().getName() + name + " " + i);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //信号量变更
                ticket = name % 3 + 1;
                return;

            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> foo(1));
        Thread thread2 = new Thread(() -> foo(2));
        Thread thread3 = new Thread(() -> foo(3));
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
