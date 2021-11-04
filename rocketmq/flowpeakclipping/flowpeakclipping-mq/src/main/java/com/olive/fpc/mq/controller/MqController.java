package com.olive.fpc.mq.controller;

import com.alibaba.fastjson.JSON;
import com.olive.fpc.mq.response.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 15:54
 */
@RestController
@Log4j2
public class MqController {

    // 用defaultproducer发送消息
    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${rocketmq.topic.getMessageTopic}")
    private String getMessageTopic;

    @PostMapping("/getMessage")
    public BaseResponse getMessage(@RequestBody Object jsonData) throws InterruptedException {
        try {
            String jsonString = JSON.toJSONString(jsonData);
            Message msg = new Message(getMessageTopic/*Topic*/,
                    "*" /*Tag*/,
                    jsonString.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */);
            SendResult sendResult = defaultMQProducer.send(msg);
            String msgId = sendResult.getMsgId();
            SendStatus sendStatus = sendResult.getSendStatus();

            // 如果sendStatus是发送成功的状态，则在30秒内尝试在redis根据msgId获取消费者业务处理的结果,15秒后没拿到则返回超时
            int i = 30;
            String consumerResult = null;
            if (StringUtils.equals(sendStatus.toString(), "SEND_OK")) {
                while (i > 0) {
                    consumerResult = stringRedisTemplate.opsForValue().get(msgId);
                    if (consumerResult != null) {
                        BaseResponse baseResponse = new BaseResponse();
                        baseResponse.setMessage(consumerResult + msgId);

                        return baseResponse;
                    }
                    Thread.sleep(1000);
                    i--;
                }
                return new BaseResponse(500001, "请求返回超时");
            }

            System.out.printf("%s%n", sendStatus);
        } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(1000);
        }

        return new BaseResponse(500, "生产者发送消息失败");
    }

}
