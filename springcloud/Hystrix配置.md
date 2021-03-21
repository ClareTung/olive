# Hystrix配置

```yaml
hystrix:
  threadpool:
    default:
      coreSize: 200 #并发执行的最大线程数，默认10
      maxQueueSize: 1000 #BlockingQueue的最大队列数，默认值-1
      queueSizeRejectionThreshold: 800 #即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝，默认值5

```

* 官方默认队列阈值只有5个， 如果要调整队列，必须同时修改maxQueueSize和queueSizeRejectionThreshold属性的值，否则都会出现异常！


```
java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@7ed27b0 rejected from java.util.concurrent.ThreadPoolExecutor@70927bf[Running, pool size = 10, active threads = 10, queued tasks = 0, completed tasks = 2671]
	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2047)
	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:823)
	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1369)
	at java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:112)
	at com.netflix.hystrix.strategy.concurrency.HystrixContextScheduler$ThreadPoolWorker.schedule(HystrixContextScheduler.java:172)
	at com.netflix.hystrix.strategy.concurrency.HystrixContextScheduler$HystrixContextSchedulerWorker.schedule(HystrixContextScheduler.java:106)
```
