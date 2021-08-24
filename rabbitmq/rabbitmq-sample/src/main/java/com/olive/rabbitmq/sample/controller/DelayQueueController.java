package com.olive.rabbitmq.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 延时队列生产者
 * @program: olive
 * @author: dtq
 * @create: 2021/8/24 14:06
 */
@RestController
public class DelayQueueController {
    private static final Logger log = LoggerFactory.getLogger(DelayQueueController.class);
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private Environment env;

    @GetMapping(value = "delay/queue/send")
    public String send(@RequestParam String message) {
        try {
            rabbitTemplate.setExchange(env.getProperty("simple.produce.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("simple.produce.routing.key.name"));

            Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(message)).build();
            rabbitTemplate.convertAndSend(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("发送消息完毕----");

        return "success";
    }
}
