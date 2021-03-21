package com.olive.java.start.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface EnableRedisLock {
    String lockeKey();

    long expireTime() default 5;

    TimeUnit timeUnit() default TimeUnit.MINUTES;

    int retryTimes() default 10;
}
