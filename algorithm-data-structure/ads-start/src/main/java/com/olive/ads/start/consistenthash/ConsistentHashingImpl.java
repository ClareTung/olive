package com.olive.ads.start.consistenthash;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description: 一致性Hash算法实现
 * @program: olive
 * @author: dtq
 * @create: 2021/9/29 11:34
 */
public class ConsistentHashingImpl implements ConsistentHashing {

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashingImpl.class);

    /**
     * 虚拟节点名称模板
     */
    private static final String virtualNodeFormat = "%s&&VN%s";

    /**
     * 真实节点对应虚拟节点
     */
    private SortedMap<String, List<String>> realNodeToVirtualNode;

    /**
     * hash值对应节点
     */
    private SortedMap<Integer, String> hashToNodes;

    /**
     * 节点对应数据
     */
    private Map<String, List<String>> nodeToData;

    /**
     * 每个节点对应虚拟节点数量
     */
    private int virtualNodeNum;

    public ConsistentHashingImpl() {
        this(0, new String[0]);
    }

    public ConsistentHashingImpl(String... nodes) {
        this(0, nodes);
    }

    public ConsistentHashingImpl(int virtualNodeNum) {
        this(virtualNodeNum, new String[0]);
    }

    public ConsistentHashingImpl(int virtualNodeNum, String... nodes) {
        if (virtualNodeNum < 0) {
            // 虚拟节点不能小于0
            throw new IllegalArgumentException();
        }
        // 初始化成员属性
        this.virtualNodeNum = virtualNodeNum;
        realNodeToVirtualNode = new TreeMap<>();
        hashToNodes = new TreeMap<>();
        nodeToData = new HashMap<>();
        for (String server : nodes) {
            hashToNodes.put(getHash(server), server);
            nodeToData.put(server, new LinkedList<>());
        }
        // 虚拟节点数大于0，增加虚拟节点数
        if (virtualNodeNum > 0) {
            for (String server : nodes) {
                addVirtualNode(server);
            }
        }
    }

    @Override
    public boolean batchPutData(List<String> data) {
        // 循环调用put data方法将数据添加到循环中
        for (String incomingData : data) {
            if (!putData(incomingData)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean putData(String data) {
        if (hashToNodes.isEmpty()) {
            // 可用节点为空
            return false;
        }
        // 计算data的Hash值
        int currentHash = getHash(data);
        // 获取普通节点（节点的哈希值大于数据的哈希值），若普通节点列表为空，则获取循环中的第一个节点
        SortedMap<Integer, String> usableNodes = hashToNodes.tailMap(currentHash);
        String node = usableNodes.isEmpty() ? hashToNodes.get(hashToNodes.firstKey()) : usableNodes.get(usableNodes.firstKey());
        // 添加data到节点
        List<String> dataList = nodeToData.get(node);
        dataList.add(data);
        // data添加到了节点当中去了
        return true;
    }

    @Override
    public boolean removeNode(String node) {
        // 计算移除的节点Hash值
        int removeServerHash = getHash(node);
        if (!hashToNodes.containsKey(removeServerHash)) {
            // 当前服务中不包含节点，请检查服务ip
            return false;
        }
        // 获取节点包含的数据
        List<String> removeServerData = nodeToData.get(node);
        // 获取节点对应的虚拟节点，移除所有虚拟节点
        if (virtualNodeNum != 0) {
            for (String virtualNode : realNodeToVirtualNode.get(node)) {
                removeServerData.addAll(nodeToData.get(virtualNode));
                hashToNodes.remove(getHash(virtualNode));
                nodeToData.remove(virtualNode);
            }
        }
        // 移除节点自身
        hashToNodes.remove(removeServerHash);
        nodeToData.remove(node);
        if (hashToNodes.size() == 0) {
            // 移除服务后，服务节点列表为空
            return true;
        }
        // 将移除的节点对应的数据放入一致性Hash环中
        batchPutData(removeServerData);
        // 移除节点成功
        return true;
    }

    @Override
    public boolean addNode(String node) {
        // 获取节点的Hash值
        int addServerHash = getHash(node);
        // 添加节点、迁移数据
        if (hashToNodes.isEmpty()) {
            // Hash对应节点列表为空直接添加节点和虚拟节点
            hashToNodes.put(addServerHash, node);
            nodeToData.put(node, new LinkedList<>());
            if (virtualNodeNum > 0) {
                addVirtualNode(node);
            }
        } else {
            // 获取数据并迁移
            SortedMap<Integer, String> greatServers = hashToNodes.tailMap(addServerHash);
            // 获取上一个服务
            String greatServer = greatServers.isEmpty() ? hashToNodes.get(hashToNodes.firstKey()) : greatServers.get(greatServers.firstKey());
            List<String> firstGreatServerData = new LinkedList<>(nodeToData.get(greatServer));
            // 添加节点和虚拟节点
            hashToNodes.put(addServerHash, node);
            nodeToData.put(greatServer, new LinkedList<>());
            nodeToData.put(node, new LinkedList<>());
            if (virtualNodeNum != 0) {
                addVirtualNode(node);
            }
            // 迁移数据
            batchPutData(firstGreatServerData);
        }
        // 节点添加成功
        return true;
    }

    @Override
    public void printAllData() {
        nodeToData.forEach((server, data) -> log.info("server {} contains data {}", server, data));
    }

    private void addVirtualNode(String realNode) {
        if (virtualNodeNum > 0) {
            List<String> virtualNodeList = new LinkedList<>();
            for (int cnt = 0; cnt < virtualNodeNum; cnt++) {
                // 生成虚拟节点
                String virtualNodeName = String.format(virtualNodeFormat, realNode, cnt);
                // 计算节点Hash值
                int virtualNodeHash = getHash(virtualNodeName);
                // Hash值已经存在
                if (hashToNodes.containsKey(virtualNodeHash)) {
                    continue;
                }
                // 增加节点
                virtualNodeList.add(virtualNodeName);
                hashToNodes.put(virtualNodeHash, virtualNodeName);
                nodeToData.put(virtualNodeName, new LinkedList<>());
            }
            realNodeToVirtualNode.put(realNode, virtualNodeList);
        }
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     *
     * @param data
     * @return
     */
    private int getHash(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }


}
