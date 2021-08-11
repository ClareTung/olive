package com.olive.java.start.concurrent.rootcause;

/**
 * @description: 并发问题的根本原因
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/9 11:46
 */
public class ConcurrencyRootCause {
    private int count = 0;

    private void addCount() {
        count++;
    }

    private int executeCount() throws InterruptedException {
        Thread thread0 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                addCount();
            }
        });

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                addCount();
            }
        });

        thread0.start();
        thread1.start();

        // 等待线程执行完成，返回count
        thread0.join();
        thread1.join();

        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        int count = new ConcurrencyRootCause().executeCount();
        System.out.println("count := " + count);
    }
}
