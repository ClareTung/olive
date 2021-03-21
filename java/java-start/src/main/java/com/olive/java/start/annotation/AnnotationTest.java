package com.olive.java.start.annotation;

import java.util.concurrent.TimeUnit;

public class AnnotationTest {

    @EnableRedisLock(lockeKey = "id", expireTime = 10, timeUnit = TimeUnit.SECONDS, retryTimes = 5)
    public void method1(){

    }
}
