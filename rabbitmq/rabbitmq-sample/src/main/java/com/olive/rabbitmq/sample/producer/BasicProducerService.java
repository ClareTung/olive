package com.olive.rabbitmq.sample.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/8 18:19
 */
@Service
public class BasicProducerService {
    @Resource
    private Environment env;

    //springboot中只需在properties中配置了访问RabbitMQ的用户名和密码之后，直接引入rabbitTemplate即可
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ObjectMapper objectMapper;

    public void producerSimpleMessage(String message) throws JsonProcessingException {
        rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchange.name"));
        rabbitTemplate.setRoutingKey(Objects.requireNonNull(env.getProperty("basic.info.mq.routing.key.name")));
        Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(message)).build();
        rabbitTemplate.convertAndSend(msg);
    }

}
