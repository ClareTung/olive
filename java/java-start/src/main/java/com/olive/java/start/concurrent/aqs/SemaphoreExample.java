package com.olive.java.start.concurrent.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @description: 可以控制某个资源可被同时访问的个数。
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/6 11:32
 */
@Slf4j
public class SemaphoreExample {
    private static final int THREAD_COUNT = 100;

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < THREAD_COUNT; i++) {
            int threadNum = i;

            exec.execute(() -> {
                try {
                    // 获得一个许可
                    // semaphore.acquire();
                    // 获取多个许可
                    semaphore.acquire(3);
                    test(threadNum);
                    // 释放一个许可
                    // semaphore.release();
                    semaphore.release(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
    }

    private static void test(int threadNum) throws InterruptedException {
        log.info("{}", threadNum);
        Thread.sleep(1000);
    }
}
