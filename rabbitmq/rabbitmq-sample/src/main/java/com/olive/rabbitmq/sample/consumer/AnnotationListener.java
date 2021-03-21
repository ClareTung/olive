package com.olive.rabbitmq.sample.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/8 16:38
 */
@Component
public class AnnotationListener {

    private static final Logger log = LoggerFactory.getLogger(AnnotationListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.sample.queue}")
    public void consumerMessage(@Payload byte[] message) {
        String result = null;
        try {
            result = objectMapper.readValue(message, String.class);
            log.info("注解的方式消费消息:{}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("接收到的消息：{}", result);
    }


}
