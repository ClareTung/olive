package com.olive.springboot.start.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class AsyncTaskExecutorConfig {

    /**
     * 重写AsyncTaskExecutor对象，实现全局异步线程，即@Async注解需指定线程池
     */
    @Bean(value = "asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("defineAsyncTask-");
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
       /*
        线程池对拒绝任务的处理策略(rejection policy)：
        当线程池已经达到最大线程数量，没有空闲线程时，新任务该如何处理
        可选策略：
        CallerRunsPolicy:当线程池没有能力处理时直接在执行方法的调用线程中运行被拒绝的任务
        如果执行程序已经关闭，将丢弃该任务.
        AbortPolicy:处理程序遭到拒绝时将抛出 RejectedExecutionException
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待所有任务调度完成在关闭线程池，保证所有的任务被正确处理
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //线程池关闭时等待其他任务的时间，不能无限等待，确保应用最后能被关闭。而不是无限期阻塞
        executor.setAwaitTerminationSeconds(60);
        //线程池初始化
        executor.initialize();
        return executor;
    }

}
