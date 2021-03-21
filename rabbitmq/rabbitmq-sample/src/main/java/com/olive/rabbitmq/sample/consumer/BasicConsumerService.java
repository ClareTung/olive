package com.olive.rabbitmq.sample.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/8 18:08
 */
@Component
public class BasicConsumerService {
    private static final Logger log = LoggerFactory.getLogger(BasicConsumerService.class);

    @Resource
    private Environment env;

    //RabbitListener注解中一个bindings就是一个绑定关系，这个关系绑定了交换机和队列。
    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "${basic.info.mq.queue.name}", durable = "true")
                    , exchange = @Exchange(value = "${basic.info.mq.exchange.name}", type = ExchangeTypes.TOPIC)
                    , key = "${basic.info.mq.routing.key.name}")
            //指定我们配置的containerFactory
            , containerFactory = "simpleRabbitListenerContainerFactory"
    )
    //@Header注解直接从消息中取出tag
    public void consumeMessage(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag, Channel channel) {
        log.info("当前线程:{},收到的简单消息:{}", Thread.currentThread().getName(), message);
        try {
            //消息确认
            log.info("{},消息确认完成", message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
