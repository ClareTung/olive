package com.olive.java.start.annotation;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.concurrent.TimeUnit;

@Aspect
public class RedisLockAspect {

    @Around("@annotation(enableRedisLock)")
    public void handleRedisLock(ProceedingJoinPoint joinPoint, EnableRedisLock enableRedisLock)
        throws Throwable{
        String lockeKey = enableRedisLock.lockeKey();
        long expireTime = enableRedisLock.expireTime();
        TimeUnit timeUnit = enableRedisLock.timeUnit();
        int retryTimes = enableRedisLock.retryTimes();

        // 获取锁
        if(tryLock(lockeKey, expireTime, timeUnit, retryTimes)){
            try {
                // 获取锁成功继续执行业务逻辑
                joinPoint.proceed();
            } finally {
                releaseLock();
            }
        }

    }

    private void releaseLock() {
    }

    private boolean tryLock(String lockeKey, long expireTime, TimeUnit timeUnit, int retryTimes) {
        return true;
    }
}
