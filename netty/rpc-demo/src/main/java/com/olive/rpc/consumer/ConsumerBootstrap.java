package com.olive.rpc.consumer;

import com.olive.rpc.common.HelloService;
import com.olive.rpc.netty.NettyClient;

/**
 * 服务消费者
 */
public class ConsumerBootstrap {
    //这里定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws Exception {

        //创建一个消费者
        NettyClient customer = new NettyClient();

        //创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);

        for (; ; ) {
            Thread.sleep(2 * 1000);
            //通过代理对象调用服务提供者的方法(服务)
            String res = service.hello("你好 rpc~");
            System.out.println("调用的结果 res= " + res);
        }
    }
}
