package com.olive.rabbitmq.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/8 16:33
 */
@RestController
public class RabbitController {

    private static final Logger log = LoggerFactory.getLogger(RabbitController.class);

    private static final String prefix = "/rabbit";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private Environment env;


    @RequestMapping(value = prefix + "/sample", method = RequestMethod.GET)
    public String sendMessage(String message) {
        log.info("待发送消息:{}", message);
        try {
            //多线程发送消息
            for (int i = 0; i < 100; i++) {
                new Thread(new sendThread(rabbitTemplate, objectMapper, message + i)).start();
            }

        } catch (Exception e) {
            log.error("发送消息异常:{}", e.fillInStackTrace());
        }
        return "success";
    }

    private class sendThread implements Runnable {

        private RabbitTemplate rabbitTemplate;
        private ObjectMapper objectMapper;
        private String message;


        public sendThread(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, String message) {
            this.rabbitTemplate = rabbitTemplate;
            this.objectMapper = objectMapper;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                rabbitTemplate.setExchange(env.getProperty("rabbitmq.sample.exchange"));
                rabbitTemplate.setRoutingKey(Objects.requireNonNull(env.getProperty("rabbitmq.sample.routing.key")));
                Message msg = null;

                msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(message))
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();
                rabbitTemplate.convertAndSend(msg);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }


}
