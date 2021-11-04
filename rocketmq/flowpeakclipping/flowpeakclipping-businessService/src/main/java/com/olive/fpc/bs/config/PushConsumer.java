package com.olive.fpc.bs.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:31
 */
@Data
@Configuration
@ToString
public class PushConsumer {
    @Value("${rocketmq.namesrvAddr}")
    private String groupName;

    @Value("${rocketmq.consumer.groupName}")
    private String namesrvAddr;
}

