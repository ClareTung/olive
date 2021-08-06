package com.olive.java.start.concurrent.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/6 13:35
 */
@Slf4j
public class CyclicBarrierExample {
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 11; i++) {
            final int threadNum = i;
            Thread.sleep(1000);
            executorService.execute(() -> {
                try {
                    race(threadNum);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }

    private static void race(int threadNum) throws InterruptedException, BrokenBarrierException, TimeoutException {
        Thread.sleep(1000);
        log.info("{} is ready", threadNum);
        // cyclicBarrier.await();
        // 超时等待：TimeoutException
        cyclicBarrier.await(7000, TimeUnit.MILLISECONDS);
        log.info("{} is continue", threadNum);
    }
}
