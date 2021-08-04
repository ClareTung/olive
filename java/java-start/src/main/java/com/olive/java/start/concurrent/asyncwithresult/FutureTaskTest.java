package com.olive.java.start.concurrent.asyncwithresult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @description: FutureTask异步返回结果
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/4 11:15
 */
public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> "FutureTask测试返回结果");
        new Thread(futureTask).start();
        System.out.println(futureTask.get());


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask1 = new FutureTask<>(() -> "FutureTask结合线程池返回结果");
        executorService.submit(futureTask1);
        System.out.println(futureTask1.get());
        executorService.shutdown();
    }
}
