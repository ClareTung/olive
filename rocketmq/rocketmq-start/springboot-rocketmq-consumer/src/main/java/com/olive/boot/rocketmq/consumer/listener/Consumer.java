package com.olive.boot.rocketmq.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/1 11:17
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "tp_springboot_01", consumerGroup = "springboot-mq-consumer-1")
public class Consumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("receive message :" + message);
    }
}
