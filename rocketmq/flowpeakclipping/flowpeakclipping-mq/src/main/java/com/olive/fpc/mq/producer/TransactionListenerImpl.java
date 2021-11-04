package com.olive.fpc.mq.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 17:40
 */
public class TransactionListenerImpl implements TransactionListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();
    ;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        // 在这个方法里执行需要用到事务的业务的业务操作
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTrans.put(message.getTransactionId(), status);
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        Integer status = localTrans.get(messageExt.getTransactionId());
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW; // 未知
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE; // 回滚
                default:
                    return LocalTransactionState.COMMIT_MESSAGE;
            }
        }

        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
