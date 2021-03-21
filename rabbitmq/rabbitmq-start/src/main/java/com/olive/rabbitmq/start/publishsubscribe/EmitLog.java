package com.olive.rabbitmq.start.publishsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/7 17:07
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 广播
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//
//            String message = argv.length < 1 ? "info: Hello World!" :
//                    String.join(" ", argv);

            for (int i = 0; i < 3; i++) {
                String message = "pub sub";

                // 第二个参数为空，匿名转发
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

}
