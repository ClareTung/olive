package com.olive.fpc.bs.consumerconfigure;

import com.olive.fpc.bs.config.PullConsumer;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.MQPullConsumer;
import org.apache.rocketmq.client.consumer.MQPullConsumerScheduleService;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullTaskContext;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:33
 */
@Log4j2
@Configuration
public abstract class DefaultPullConsumerConfigure {

    @Resource
    private PullConsumer pullConsumer;

    // 开启消费者监听服务
    public void listener(String topic, String tag) throws MQClientException {
        log.info("开启" + topic + ":" + tag + "消费者-------------------------");
        log.info(pullConsumer.toString());

        MQPullConsumerScheduleService scheduleService = new MQPullConsumerScheduleService(pullConsumer.getGroupName());
        scheduleService.getDefaultMQPullConsumer().setNamesrvAddr(pullConsumer.getNamesrvAddr());
        scheduleService.setMessageModel(MessageModel.CLUSTERING);

        scheduleService.registerPullTaskCallback(topic, (mq, context) -> {
            PullTaskContext pullTaskContext;
            MQPullConsumer consumer = context.getPullConsumer();

            try {
                long offset = consumer.fetchConsumeOffset(mq, false);
                if (offset < 0) {
                    offset = 0;
                }

                PullResult pullResult = consumer.pull(mq, tag, offset, 32);
                switch (pullResult.getPullStatus()) {
                    case FOUND:
                        List<MessageExt> msgs = pullResult.getMsgFoundList();
                        DefaultPullConsumerConfigure.this.dealBody(msgs);
                    case NO_MATCHED_MSG:
                        break;
                    case NO_NEW_MSG:
                    case OFFSET_ILLEGAL:
                        break;
                    default:
                        break;
                }

                consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
                //rocketmq默认更新消费者队列的offset的时间5s，这里为1毫秒，即每隔一毫秒拉取消息
                context.setPullNextDelayTimeMillis(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        scheduleService.start();
        log.info("rocketmq启动成功---------------------------------------");
    }

    // 处理body的业务
    public abstract void dealBody(List<MessageExt> msgs);
}
