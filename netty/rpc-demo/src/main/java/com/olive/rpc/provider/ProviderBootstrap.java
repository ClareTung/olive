package com.olive.rpc.provider;

import com.olive.rpc.netty.NettyServer;

/**
 * 服务提供者启动类
 */
public class ProviderBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
