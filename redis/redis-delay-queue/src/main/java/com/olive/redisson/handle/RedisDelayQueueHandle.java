package com.olive.redisson.handle;

/**
 * @description: 延迟队列执行器
 * @program: dtq
 * @author: dtq
 * @create: 2021/8/12 18:13
 */
public interface RedisDelayQueueHandle<T> {

    void execute(T t);

}
