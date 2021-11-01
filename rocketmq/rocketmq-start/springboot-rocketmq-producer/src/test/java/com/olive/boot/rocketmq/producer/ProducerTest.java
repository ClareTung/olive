package com.olive.boot.rocketmq.producer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/10/30 13:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootMqProducerApplication.class})
public class ProducerTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSendMessage() {
        // 用于向broker发送消息
        // 第一个参数是topic名称
        // 第二个参数是消息内容
        rocketMQTemplate.convertAndSend(
                "tp_springboot_01",
                "springboot: hello"
        );
    }

    @Test
    public void testSendMessages() {
        for (int i = 0; i < 100; i++) {
            // 用于向broker发送消息
            // 第一个参数是topic名称
            // 第二个参数是消息内容
            rocketMQTemplate.convertAndSend(
                    "tp_springboot_01",
                    "springboot: hello " + i
            );
        }
    }

}
