package com.olive.java.start.threadorderexecute;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock和信号量实现
 */
public class UseLock {
    AtomicInteger ticket = new AtomicInteger(1);
    public Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();
    private Condition[] conditions = {condition1, condition2, condition3};

    public void foo(int name) {
        try {
            lock.lock();
            //因为线程的执行顺序是不可预期的，因此需要每个线程自旋
            System.out.println("线程" + name + " 开始执行");
            if (ticket.get() != name) {
                try {
                    System.out.println("当前标识位为" + ticket.get() + ",线程" + name + " 开始等待");
                    //开始等待被唤醒
                    conditions[name - 1].await();
                    System.out.println("线程" + name + " 被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name);
            ticket.getAndIncrement();
            if (ticket.get() > 3) {
                ticket.set(1);
            }
            //执行完毕，唤醒下一次。1唤醒2,2唤醒3
            conditions[name % 3].signal();
        } finally {
            //一定要释放锁
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        UseLock example = new UseLock();
        Thread t1 = new Thread(() -> {
            example.foo(1);
        });
        Thread t2 = new Thread(() -> {
            example.foo(2);
        });
        Thread t3 = new Thread(() -> {
            example.foo(3);
        });
        t1.start();
        t2.start();
        t3.start();
    }
}

