package com.olive.fpc.bs.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.olive.fpc.bs.consumerconfigure.DefaultPullConsumerConfigure;
import com.olive.fpc.bs.entity.TestBean;
import com.olive.fpc.bs.service.TestService;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:32
 */
@Log4j2
@Configuration
public class FpcConsumer extends DefaultPullConsumerConfigure implements ApplicationRunner {

    @Resource
    private TestService testService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${rocketmq.topic.getMessageTopic}")
    private String getMessageTopic;

    @Override
    public void dealBody(List<MessageExt> msgs) {
        // 从消息队列拿到数据进行业务处理
        log.info("拉取消息");

        msgs.stream().forEach(msg -> {
            try {
                String msgStr = new String(msg.getBody(), "utf-8");
                JSONObject jsonObject = JSON.parseObject(msgStr);

                TestBean testBean = new TestBean();
                testBean.setMessage(jsonObject.getString("message"));
                // 这里可以进行业务操作
                String operationResult = testService.operation(testBean);
                // 将业务操作的结果放入redis缓存
                stringRedisTemplate.opsForValue().set(msg.getMsgId(), operationResult);
                log.info(operationResult + msg.getMsgId() + new Date());
            } catch (UnsupportedEncodingException e) {
                log.error("body转字符串解析失败");
            }
        });

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            super.listener(getMessageTopic, "*");
        } catch (MQClientException e) {
            log.error("消费者监听器启动失败", e);
        }
    }
}
