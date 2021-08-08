package com.olive.rpc.common;

/**
 * 消费者和服务提供者共同调用
 */
public interface HelloService {

    /**
     * 当有消费方调用该方法时， 就返回一个结果
     *
     * @param mes
     * @return
     */
    String hello(String mes);
}
