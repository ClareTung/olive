package com.olive.rabbitmq.start.topic;

import com.google.common.base.Strings;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/7 18:13
 */
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            //所有设备和日志级别
            String[] facilities = {"auth", "cron", "kern", "auth.A"};
            String[] severities = {"error", "info", "warning"};

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    //每一个设备，每种日志级别发送一条日志消息
                    String routingKey = facilities[i] + "." + severities[j % 3];

                    // 发送的消息
                    String message = " Hello World!" + Strings.repeat(".", i + 1);
                    //参数1：exchange name
                    //参数2：routing key
                    channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
                }
            }
        }
    }


}
