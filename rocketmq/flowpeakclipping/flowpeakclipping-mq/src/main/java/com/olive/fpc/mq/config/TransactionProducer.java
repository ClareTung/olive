package com.olive.fpc.mq.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 17:21
 */
@Data
@ToString
@Configuration
public class TransactionProducer {

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.transactionProducer.groupName}")
    private String groupName;
}
