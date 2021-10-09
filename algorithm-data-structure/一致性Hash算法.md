[TOC]

## 一致性Hash算法

### 算法原理

```
先构造一个长度为2^32的整数环（这个环被称为一致性Hash环），根据节点名称的Hash值（其分布为[0, 2^32-1]）将服务器节点放置在这个Hash环上，然后根据数据的Key值计算得到其Hash值（其分布也为[0, 2^32-1]），接着在Hash环上顺时针查找距离这个Key值的Hash值最近的服务器节点，完成Key到服务器的映射查找。
```

* 这种算法解决了**普通余数Hash算法伸缩性差**的问题，可以保证在上线、下线服务器的情况下尽量有多的请求命中原来路由到的服务器。

![微信图片编辑_20210929111233](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87%E7%BC%96%E8%BE%91_20210929111233.jpg)

### 数据结构选取

#### 红黑树

* 红黑树存储的数据有序
* JDK中提供了红黑树的代码实现TreeMap和TreeSet
* TreeMap提供了一个tailMap(K fromKey)方法，支持从红黑树中查找比fromKey大的值的集合，并不需要遍历整个数据结构
* 红黑树的查找时间复杂度为O(logN)

> 常见的时间复杂度与时间效率的关系有如下的经验规则：
>
> **O(1) < O(log2N) < O(N) < O(N \* log2N) < O(N2) < O(N3)  < O(N!)**

* 数据插入效率TreeMap性能比ArrayList和LinkedList慢5~10倍

### Hash值重新计算

* 字符串默认hashCode()方法得到的值，会比较连续，分散性不够，导致很大一部分路由到了某一个节点上
* 重新计算Hash值的算法有很多，比如CRC32_HASH、FNV1_32_HASH、KETAMA_HASH等

### 使用虚拟节点改善一致性Hash算法

* 比如说有Hash环上有A、B、C三个服务器节点，分别有100个请求会被路由到相应服务器上。现在在A与B之间增加了一个节点D，这导致了原来会路由到B上的部分节点被路由到了D上，这样A、C上被路由到的请求明显多于B、D上的，原来三个服务器节点上均衡的负载被打破了。**某种程度上来说，这失去了负载均衡的意义，因为负载均衡的目的本身就是为了使得目标服务器均分所有的请求**。

* 解决这个问题的办法是引入虚拟节点，其工作原理是：**将一个物理节点拆分为多个虚拟节点，并且同一个物理节点的虚拟节点尽量均匀分布在Hash环上**。采取这样的方式，就可以有效地解决增加或减少节点时候的负载不均衡的问题。
* 至于一个物理节点应该拆分为多少虚拟节点，下面可以先看一张图：

![微信图片编辑_20210929112147](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87%E7%BC%96%E8%BE%91_20210929112147.jpg)

* 横轴表示需要为每台物理服务器扩展的虚拟节点倍数，纵轴表示的是实际物理服务器数。可以看出，物理服务器很少，需要更大的虚拟节点；反之物理服务器比较多，虚拟节点就可以少一些。比如有10台物理服务器，那么差不多需要为每台服务器增加100~200个虚拟节点才可以达到真正的负载均衡。

### Java代码实现

```java
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
```

```java
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
```

```java
/**
 * @description: 一致性Hash算法测试
 * @program: olive
 * @author: dtq
 * @create: 2021/9/29 14:23
 */
public class ConsistentHashingTest {
    
    public static void main(String[] args) {

        String[] servers = new String[]{"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
                "192.168.0.3:111", "192.168.0.4:111"};
        ConsistentHashing consistentHashing = new ConsistentHashingImpl(5, servers);
        String[] data = new String[]{"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};

        for (String str : data) {
            consistentHashing.putData(str);
        }
        consistentHashing.removeNode("192.168.0.1:111");
        consistentHashing.addNode("192.168.0.5:111");
        consistentHashing.putData("10.185.0.1:4444");
        consistentHashing.printAllData();
    }
}
```



