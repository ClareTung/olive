[toc]

## ES的分布式架构原理（ES是如何实现分布式的）

* ElasticSearch 设计的理念就是分布式搜索引擎，底层其实还是基于 lucene 的。
* 核心思想就是在多台机器上启动多个 ES 进程实例，组成了一个 ES 集群。
* ES 中存储数据的**基本单位是索引**，比如说你现在要在 ES 中存储一些订单数据，你就应该在 ES 中创建一个索引 `order_idx` ，所有的订单数据就都写到这个索引里面去。

```
index -> type -> mapping -> document -> field
```

* 一个 index 里可以有多个 type，每个 type 的字段都是差不多的，但是有一些略微的差别。
* 每个 type 有一个 mapping，而 mapping 就是这个 type 的**表结构定义**。实际上你往 index 里的一个 type 里面写的一条数据，叫做一条 document，每个 document 有多个 field，每个 field 就代表了这个 document 中的一个字段的值。

* 一个索引可以拆分成多个 `shard` ，每个 shard 存储部分数据。拆分多个 shard 是有好处的，一是**支持横向扩展**，比如你数据量是 3T，3 个 shard，每个 shard 就 1T 的数据，若现在数据量增加到 4T，怎么扩展，很简单，重新建一个有 4 个 shard 的索引，将数据导进去；二是**提高性能**，数据分布在多个 shard，即多台服务器上，所有的操作，都会在多台机器上并行分布式执行，提高了吞吐量和性能。
* 这个 shard 的数据实际是有多个备份，就是说每个 shard 都有一个 `primary shard` ，负责写入数据，但是还有几个 `replica shard` 。 `primary shard` 写入数据之后，会将数据同步到其他几个 `replica shard` 上去。

![image-20210930141412699](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210930141412699.png)

* 通过这个 replica 的方案，每个 shard 的数据都有多个备份，如果某个机器宕机了，没关系啊，还有别的数据副本在别的机器上呢。高可用了吧。
* ES 集群多个节点，会自动选举一个节点为 master 节点，这个 master 节点其实就是干一些管理的工作的，比如维护索引元数据、负责切换 primary shard 和 replica shard 身份等。要是 master 节点宕机了，那么会重新选举一个节点为 master 节点。
* 如果是非 master 节点宕机了，那么会由 master 节点，让那个宕机节点上的 primary shard 的身份转移到其他机器上的 replica shard。接着你要是修复了那个宕机机器，重启了之后，master 节点会控制将缺失的 replica shard 分配过去，同步后续修改的数据之类的，让集群恢复正常。
* 说得更简单一点，就是说如果某个非 master 节点宕机了。那么此节点上的 primary shard 不就没了。那好，master 会让 primary shard 对应的 replica shard（在其他机器上）切换为 primary shard。如果宕机的机器修复了，修复后的节点也不再是 primary shard，而是 replica shard。







