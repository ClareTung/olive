package com.olive.springboot.start.schedule;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 暂时关闭20220507，要测试可以打开@Component
 */
// @Component
public class ScheduledTask {

    @Scheduled(cron = "0/1 * * * * ?")
    @Async
    public void scheduledTask1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask1 " + System.currentTimeMillis());

        // 定时任务阻塞
        /*while (true) {
            System.out.println("task1 sleep");
            Thread.sleep(1000);
        }*/
    }

    @Scheduled(cron = "0/1 * * * * ?")
    @Async("asyncTaskExecutor")
    public void scheduledTask2() {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask2 " + System.currentTimeMillis());
    }
}
