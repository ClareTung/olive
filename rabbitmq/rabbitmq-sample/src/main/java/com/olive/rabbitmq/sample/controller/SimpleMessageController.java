package com.olive.rabbitmq.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olive.rabbitmq.sample.producer.BasicProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/8 18:43
 */
@RestController
public class SimpleMessageController {
    private static final Logger log = LoggerFactory.getLogger(SimpleMessageController.class);

    @Resource
    private BasicProducerService basicProducerService;

    @RequestMapping("/send/simple")
    public String sendSimpleMessage(@RequestParam String message) throws JsonProcessingException {
        log.info("请求消息为:{}", message);
        for (int i = 0; i < 1000; i++) {//多线程的方式去发送数据
            new Thread(new SendMessageThread(message + i, basicProducerService)).start();
        }
        basicProducerService.producerSimpleMessage(message);
        return "success";
    }

    private class SendMessageThread implements Runnable {

        private String message;
        private BasicProducerService basicProducerService;

        public SendMessageThread(String message, BasicProducerService basicProducerService) {
            this.message = message;
            this.basicProducerService = basicProducerService;
        }

        @Override
        public void run() {
            try {
                basicProducerService.producerSimpleMessage(message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
