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
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @description: 延时队列消费者
 * @program: olive
 * @author: dtq
 * @create: 2021/8/24 14:03
 */
@Component
public class DelayRealDealQueueListener {
    private static final Logger log = LoggerFactory.getLogger(DelayRealDealQueueListener.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${simple.deal.queue.name}", durable = "true")
                    , exchange = @Exchange(value = "${simple.deal.exchange.name}", type = ExchangeTypes.TOPIC)
                    , key = "${simple.deal.routing.key.name}")
            , containerFactory = "simpleRabbitListenerContainerFactory"
    )
    public void dealDelayMessage(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long delivertTag, Channel channel) {
        try {
            String result = new String(message);
            log.info("开始处理真正的延时处理消息:{}", result);
//            channel.basicAck(delivertTag, true);
        } catch (Exception e) {
            log.error("延时消息异常:{}", e.fillInStackTrace());
        }
    }
}
