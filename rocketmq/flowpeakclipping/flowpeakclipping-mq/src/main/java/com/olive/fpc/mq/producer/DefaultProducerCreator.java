package com.olive.fpc.mq.producer;

import com.olive.fpc.mq.config.Producer;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 17:08
 */
@Log4j2
@Configuration
public class DefaultProducerCreator {

    @Resource
    private Producer producerConfiguration;

    /**
     * 创建普通消息发送者实例
     *
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        log.info(producerConfiguration.toString());
        log.info("defaultProduce正在创建-----------------------------------");
        DefaultMQProducer producer = new DefaultMQProducer(producerConfiguration.getGroupName());
        producer.setNamesrvAddr(producerConfiguration.getNamesrvAddr());
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendAsyncFailed(10);
        producer.start();
        log.info("rocketmq producer server启动成功-----------------------------------");
        return producer;
    }
}
