package com.olive.ads.start.consistenthash;

import java.util.List;

/**
 * @description: 一致性Hash算法
 * @program: olive
 * @author: dtq
 * @create: 2021/9/29 11:34
 */
public interface ConsistentHashing {


    /**
     * 批量添加数据进入Hash环
     *
     * @param data data list
     * @return put result
     */
    boolean batchPutData(List<String> data);

    /**
     * 添加单个数据进入Hash环
     *
     * @param data data
     * @return put result
     */
    boolean putData(String data);

    /**
     * 从Hash环中移除节点
     *
     * @param node removing node
     * @return remove result
     */
    boolean removeNode(String node);

    /**
     * 添加节点进入Hash环
     *
     * @param node adding node
     * @return add result
     */
    boolean addNode(String node);

    /**
     * 打印Hash环中所有节点数据
     */
    void printAllData();
}
