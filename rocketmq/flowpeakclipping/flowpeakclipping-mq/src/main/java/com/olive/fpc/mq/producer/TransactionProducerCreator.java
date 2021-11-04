package com.olive.fpc.mq.producer;

import com.olive.fpc.mq.config.TransactionProducer;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 17:22
 */
@Log4j2
@Configuration
public class TransactionProducerCreator {

    @Resource
    private TransactionProducer transactionProducer;

    /**
     * 创建事务消息发送者实例
     *
     * @return
     */
    @Bean
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        log.info(transactionProducer.toString());
        log.info("TransactionMQProducer 正在创建---------------------------------------");
        TransactionMQProducer producer = new TransactionMQProducer(transactionProducer.getGroupName());
        TransactionListenerImpl transactionListener = new TransactionListenerImpl();
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Runnable target;
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setNamesrvAddr(transactionProducer.getNamesrvAddr());
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();
        log.info("TransactionMQProducer server启动成功---------------------------------.");
        return producer;
    }

}
