package com.olive.java.start.concurrent.asyncwithresult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description: Future异步返回结果
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/4 11:08
 */
public class FutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> "Future测试返回结果");
        System.out.println(future.get());
        executorService.shutdown();
    }
}
