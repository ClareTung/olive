package com.olive.rabbitmq.start.routing;

import com.google.common.base.Strings;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/7 17:46
 */
public class EmitLogDirect {


    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            //所有日志严重性级别
            String[] severities = {"error", "info", "warning"};
            for (int i = 0; i < 3; i++) {
                String severity = severities[i % 3];//每一次发送一条不同严重性的日志

                // 发送的消息
                String message = "Hello World [" + severity + "]" + Strings.repeat(".", i + 1);
                //参数1：exchange name
                //参数2：routing key
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
                System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
            }
        }
    }
}
