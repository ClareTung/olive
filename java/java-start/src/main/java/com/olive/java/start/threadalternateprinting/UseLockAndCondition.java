package com.olive.java.start.threadalternateprinting;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock和condition
 *
 * 替代传统的Object的wait和notify实现线程间的协作，使用Condition的await和signal这种方式实现线程间协作更加安全和高效。
 *
 * Condition是个接口，依赖于Lock接口，lock.newCondition()生成一个Condition，调用Condition的await和signal，都必须在lock的保护之内，
 * 就是在lock.lock()和lock.unlock()之间才可以使用。
 */
public class UseLockAndCondition {

    private static int count = 0;
    private Lock lock = new ReentrantLock();

    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    Thread t1 = new Thread(() -> {
        while (true){
            try {
                lock.lock();
                while (count % 3 != 0){
                    c1.await();
                }

                System.out.println("A");
                count++;

                c2.signal(); // 唤醒条件2
            } catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    });

    Thread t2 = new Thread(() -> {
        while (true){
            try {
                lock.lock();
                while (count % 3 != 1){
                    c2.await();
                }

                System.out.println("B");
                count++;

                c3.signal(); // 唤醒条件3
            } catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    });

    Thread t3 = new Thread(() -> {
        while (true){
            try {
                lock.lock();
                while (count % 3 != 2){
                    c3.await();
                }

                System.out.println("C");
                count++;

                c1.signal(); // 唤醒条件1
            } catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    });

    public void fun(){
        t3.start();
        t1.start();
        t2.start();
    }

    public static void main(String[] args) {
        UseLockAndCondition ulc = new UseLockAndCondition();
        long t1 = System.currentTimeMillis();
        ulc.fun();
        while (true){
            if(System.currentTimeMillis() - t1 >= 10){
                System.exit(0);
            }
        }
    }

}
