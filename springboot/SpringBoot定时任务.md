[TOC]

# SpringBoot定时任务

* 启动类增加注解：@EnableScheduling

## 串行多任务

### 相同的定时任务

```java

@Component
public class ScheduledTask {

    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask1() {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask1 " + System.currentTimeMillis());
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask2() {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask2 " + System.currentTimeMillis());
    }
}
```

* 运行结果

```text
scheduling-1---scheduledTask2 1620903582007
scheduling-1---scheduledTask1 1620903583014
scheduling-1---scheduledTask2 1620903583014
scheduling-1---scheduledTask1 1620903584002
scheduling-1---scheduledTask2 1620903584002
scheduling-1---scheduledTask1 1620903585006
```

* 串行任务没有明显优先级

### 一个定时任务阻塞

```java
    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask1 " + System.currentTimeMillis());

        // 定时任务阻塞
        while (true) {
            System.out.println("task1 sleep");
            Thread.sleep(1000);
        }
    }
```

* 运行结果

```text
scheduling-1---scheduledTask1 1620955343010
task1 sleep
task1 sleep
task1 sleep
task1 sleep
task1 sleep
```

* 多个定时任务时串行执行的,如果一个任务出现阻塞，其他的任务都会受到影响。

## 多任务并行执行

* 启动类增加注解：@EnableAsync
* 任务上增加注解：@Async

```java
    @Scheduled(cron = "0/1 * * * * ?")
    @Async
    public void scheduledTask2() {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask2 " + System.currentTimeMillis());
    }
```

* 运行结果

```text
task-2---scheduledTask1 1620955818025
task-1---scheduledTask2 1620955818025
task-3---scheduledTask2 1620955819014
task-4---scheduledTask1 1620955819014
task-6---scheduledTask1 1620955820009
task-5---scheduledTask2 1620955820008
task-7---scheduledTask2 1620955821007
task-8---scheduledTask1 1620955821007
```

* springBoot2.0 和 springBoot2.2 默认的线程池是不一样的，2.0默认线程池（**SimpleAsyncTaskExecutor**）每开启一个新的任务都是新开一个线程的。2.2默认线程池有8个。
* 如果你的定时任务比较少，并且执行时间比较短其实上面两种都没啥关系。但是如果定时任务多且慢，那么2.0每次创建一个线程可能导致JVM挂掉，而2.2只有8个线程也会满足不了，会导致任务等待。

