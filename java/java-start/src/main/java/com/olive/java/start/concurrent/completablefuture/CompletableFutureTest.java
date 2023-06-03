package com.olive.java.start.concurrent.completablefuture;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: CompletableFuture测试
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/10 16:41
 */
@Slf4j
public class CompletableFutureTest {

    public static final ExecutorService CHECK_THREAD_POOL =
            new ThreadPoolExecutor(3, 10, 300L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(100),
                    new ThreadFactoryBuilder().setNameFormat("check-pool-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());


    @Test
    public void testFuture() throws Exception {
        // case:Future异步执行任务，获取结果
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Integer> future = executorService.submit(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        });

        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务执行完成，如果已完成直接返回结果
        // 如果执行任务异常，则get方法会把之前捕获的异常重新抛出
        log.info("future result is -> {}", future.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }


    @Test
    public void testSupplyAsync() throws Exception {
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        });
        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testSupplyAsync2() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);
        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testRunAsync() throws Exception {
        // case：创建异步任务，无返回值
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
            }
        });
        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }


    @Test
    public void testRunAsync2() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // case：创建异步任务，无返回值
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
            }
        }, executorService);
        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testThenApply() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        // 回调方法
        CompletableFuture<String> future1 = future.thenApply((result) -> {
            // ForkJoinPool-1-worker-1 和调用supplyAsync的线程ForkJoinPool-1-worker-1  是同一个线程
            log.info("thenApply {} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            return "A:" + result;
        });

        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("future 异步回调 result is -> {}", future1.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testThenApplyAsync() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        // 回调方法
        CompletableFuture<String> future1 = future.thenApplyAsync((result) -> {
            // [ForkJoinPool.commonPool-worker-1] 和调用supplyAsync的线程[ForkJoinPool-1-worker-1]  不是同一个线程
            log.info("thenApplyAsync {} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            return "A:" + result;
        });


        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("future 异步回调 result is -> {}", future1.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testThenAcceptAndThenRun() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        // 回调方法
        future.thenApply((result) -> {
            // ForkJoinPool-1-worker-1 和调用supplyAsync的线程ForkJoinPool-1-worker-1  是同一个线程
            log.info("thenApply {} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            return "A:" + result;
        }).thenAccept((result) -> { //接收上一个任务的执行结果作为入参，但是没有返回值
            log.info("thenAccept 接收到的参数为： {}", result);
        }).thenRun(() -> { //无入参，也没有返回值
            log.info("thenRun 不接收参数，也没有返回值");
        });

        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future result is -> {}", future.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testExceptionally() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (true) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> future1 = future.exceptionally((e) -> {
            log.info("抛出异常：{}", e.getMessage());

            return -100;
        });

        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future 异步回调 result is -> {}", future1.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testWhenComplete() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> future1 = future.whenComplete((result, e) -> {
            if (e != null) {
                log.info("抛出异常： {}", e.getMessage());
            } else {
                log.info("运行结果： {}", result); // 200
            }

            result = result + 100;
            log.info("结果值修改： {}", result); // 300
        });

        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future 异步回调 result is -> {}", future1.get()); // 200
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testHandle() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // case；创建异步任务，有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> future1 = future.handle((result, e) -> {
            if (e != null) {
                log.info("抛出异常： {}", e.getMessage());
            } else {
                log.info("运行结果： {}", result);
            }

            // 返回值值
            return result;
        });

        log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        // 等待子任务完成
        log.info("future 异步回调 result is -> {}", future1.get());
        log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }

    @Test
    public void testThenCombine() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 100;
            }
        }, pool);

        //cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,且有返回值
        CompletableFuture<Integer> cf3 = cf1.thenCombine(cf2, (a, b) -> {
            log.info("thenCombine param a: {}， b: {}", a, b);

            return a + b;
        });

        log.info("cf3 result is: {}", cf3.get());

        //cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf4,但没有返回值
        CompletableFuture<Void> cf4 = cf1.thenAcceptBoth(cf2, (a, b) -> {
            log.info("thenAcceptBoth param a: {}， b: {}", a, b);
        });

        log.info("cf4 result is: {}", cf4.get());

        // 没有入参，也没有返回值
        CompletableFuture<Void> cf5 = cf1.runAfterBoth(cf2, () -> {
            log.info("runAfterBoth 没有入参，也没有返回值");
        });

        log.info("cf5 result is: {}", cf5.get());
    }

    @Test
    public void testApplyToEither() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 100;
            }
        }, pool);

        //cf1和cf2的异步任务都执行完成后，会将先执行完成的结果作为方法入参传递给cf3,且有返回值
        CompletableFuture<Integer> cf3 = cf1.applyToEither(cf2, (result) -> {
            log.info("applyToEither param result: {}", result);

            return result;
        });

        log.info("cf3 result is: {}", cf3.get());

        //cf1和cf2的异步任务都执行完成后，会将先执行完成的结果作为方法入参传递给cf4,但没有返回值
        CompletableFuture<Void> cf4 = cf1.acceptEither(cf2, (result) -> {
            log.info("acceptEither param result: {}", result);
        });

        log.info("cf4 result is: {}", cf4.get());

        // 没有入参，也没有返回值
        CompletableFuture<Void> cf5 = cf1.runAfterEither(cf2, () -> {
            log.info("runAfterEither 没有入参，也没有返回值");
        });

        log.info("cf5 result is: {}", cf5.get());
    }

    @Test
    public void testThenCompose() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<String> cf2 = cf1.thenCompose((param) -> {
            log.info("接收到参数为：{}", param);

            return CompletableFuture.supplyAsync(() -> "cf2" + param);
        });

        log.info("cf2 result is {}", cf2.get());
    }

    @Test
    public void testAllOfAndAnyOf() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> {
            log.info("{} start, time is -> {}", Thread.currentThread(), System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (false) {
                throw new RuntimeException("test exception");
            } else {
                log.info("{} exit, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
                return 200;
            }
        }, pool);

        CompletableFuture<Void> cf4 = CompletableFuture.allOf(cf1, cf2, cf3).whenComplete((result, e) -> {
            log.info("param is: {}", result);
        });

        log.info("cf4 result : {}", cf4.get());

        CompletableFuture<Object> cf5 = CompletableFuture.anyOf(cf1, cf2, cf3).whenComplete((result, e) -> {
            log.info("param is: {}", result);
        });

        log.info("cf5 result : {}", cf5.get());

    }

    @Test
    public void testAllOf() {

        List<CompletableFuture<String>> futureList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            futureList.add(CompletableFuture.supplyAsync(() -> checkInfo(finalI), CHECK_THREAD_POOL));
        }

        CompletableFuture<Void> allFutures =
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        CompletableFuture<List<String>> resultList = allFutures.thenApply(v ->
                futureList.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList()));

        try {
            log.info("result:{}", resultList.join());
        } catch (Exception e){
            log.info("抓住异常了");
        }


        // 测试异常后其他任务还能不能执行
        List<CompletableFuture<String>> futureList2 = new ArrayList<>();
        for (int i = 4; i <= 6; i++) {
            int finalI = i;
            futureList2.add(CompletableFuture.supplyAsync(() -> checkInfo(finalI), CHECK_THREAD_POOL));
        }

        CompletableFuture<Void> allFutures2 =
                CompletableFuture.allOf(futureList2.toArray(new CompletableFuture[0]));
        CompletableFuture<List<String>> resultList2 = allFutures2.thenApply(v ->
            futureList2.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList()));

        log.info("result2:{}", resultList2.join());
        log.info("{} currentThread, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
    }


    private String checkInfo(Integer id) {
        log.info("{} exec, time is -> {}", Thread.currentThread(), System.currentTimeMillis());
        if (id == 2) {
            throw new RuntimeException("报错啦~");
            //return null;
        }

        return "SUCCESS:" + id;
    }

}
