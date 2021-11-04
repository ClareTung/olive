package com.olive.fpc.bs.consumerconfigure;

import com.olive.fpc.bs.config.PushConsumer;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:34
 */
@Log4j2
@Configuration
public abstract class DefaultPushConsumerConfigure {

    @Resource
    private PushConsumer pushConsumer;

    // 开启消费者监听服务
    public void listener(String topic, String tag) throws MQClientException {
        log.info("开启" + topic + ":" + tag + "消费者-------------------");
        log.info(pushConsumer.toString());

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(pushConsumer.getGroupName());
        consumer.setNamesrvAddr(pushConsumer.getNamesrvAddr());
        consumer.subscribe(topic, tag);

        // 开启内部类实现监听
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                return DefaultPushConsumerConfigure.this.dealBody(msgs);
            }
        });

        consumer.start();
        log.info("rocketmq启动成功---------------------------------------");
    }

    // 处理body的业务
    public abstract ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs);
}
