## Redis-Cluster

* Redis Cluster是Redis自己提供的Redis集群化方案。
* 相对于 Codis 的不同，它是去中心化的，如图所示，该集群有三个 Redis 节点组成， 每个节点负责整个集群的一部分数据，每个节点负责的数据多少可能不一样。这三个节点相 互连接组成一个对等的集群，它们之间通过一种特殊的二进制协议相互交互集群信息。

![image-20210902173320849](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210902173320849.png)

* Redis Cluster 将所有数据划分为 16384 的 slots，它比 Codis 的 1024 个槽划分的更为 精细，每个节点负责其中一部分槽位。槽位的信息存储于每个节点中，它不像 Codis，它不 需要另外的分布式存储来存储节点槽位信息。
* 当 Redis Cluster 的客户端来连接集群时，它也会得到一份集群的槽位配置信息。这样当 客户端要查找某个 key 时，可以直接定位到目标节点。
* 这点不同于 Codis，Codis 需要通过 Proxy 来定位目标节点，RedisCluster 是直接定 位。客户端为了可以直接定位某个具体的 key 所在的节点，它就需要缓存槽位相关信息，这样才可以准确快速地定位到相应的节点。同时因为槽位的信息可能会存在客户端与服务器不 一致的情况，还需要纠正机制来实现槽位信息的校验调整。

### 槽位定位算法

* Cluster 默认会对 key 值使用 crc32 算法进行 hash 得到一个整数值，然后用这个整数 值对 16384 进行取模来得到具体槽位。 
* Cluster 还允许用户强制某个 key 挂在特定槽位上，通过在 key 字符串里面嵌入 tag 标 记，这就可以强制 key 所挂在的槽位等于 tag 所在的槽位。

### 跳转

* 当客户端向一个错误的节点发出了指令，该节点会发现指令的 key 所在的槽位并不归自 己管理，这时它会向客户端发送一个特殊的跳转指令携带目标操作的节点地址，告诉客户端 去连这个节点去获取数据。
* GET x -MOVED 3999 127.0.0.1:6381 
* MOVED 指令的第一个参数 3999 是 key 对应的槽位编号，后面是目标节点地址。 MOVED 指令前面有一个减号，表示该指令是一个错误消息。 客户端收到 MOVED 指令后，要立即纠正本地的槽位映射表。后续所有 key 将使用新 的槽位映射表

### 迁移

* Redis Cluster提供了工具redis-trib可以让运维手动调整槽位的分配情况，通过组合各种原生的Redis Cluster指令来实现。

### 容错

* Redis Cluster 可以为每个主节点设置若干个从节点，单主节点故障时，集群会自动将其 中某个从节点提升为主节点。如果某个主节点没有从节点，那么当它发生故障时，集群将完 全处于不可用状态。不过 Redis 也提供了一个参数 cluster-require-full-coverage 可以允许部分 节点故障，其它节点还可以继续提供对外访问

### 网络抖动

* 网络抖动就是非常常见的一种现象，突然之间部分连接变得不可访问，然后很快又恢复正 常。
* 为解决这种问题，Redis Cluster 提供了一种选项 cluster-node-timeout，表示当某个节点持 续 timeout 的时间失联时，才可以认定该节点出现故障，需要进行主从切换。如果没有这个 选项，网络抖动会导致主从频繁切换 (数据的重新复制)。
* 还有另外一个选项 cluster-slave-validity-factor 作为倍乘系数来放大这个超时时间来宽松容 错的紧急程度。如果这个系数为零，那么主从切换是不会抗拒网络抖动的。如果这个系数大 于 1，它就成了主从切换的松弛系数。

### 可能下线 (PFAIL-Possibly Fail) 与确定下线 (Fail)

* 因为 Redis Cluster 是去中心化的，一个节点认为某个节点失联了并不代表所有的节点都 认为它失联了。所以集群还得经过一次协商的过程，只有当大多数节点都认定了某个节点失 联了，集群才认为该节点需要进行主从切换来容错。 
* Redis 集群节点采用 Gossip 协议来广播自己的状态以及自己对整个集群认知的改变。比 如一个节点发现某个节点失联了 (PFail)，它会将这条信息向整个集群广播，其它节点也就可 以收到这点失联信息。如果一个节点收到了某个节点失联的数量 (PFail Count) 已经达到了集 群的大多数，就可以标记该节点为确定下线状态 (Fail)，然后向整个集群广播，强迫其它节 点也接收该节点已经下线的事实，并立即对该失联节点进行主从切换。
