## Redis-字典内部结构

* dict 是 Redis 服务器中出现最为频繁的复合型数据结构，除了 hash 结构的数据会用到 字典外，整个 Redis 数据库的所有 key 和 value 也组成了一个全局字典，还有带过期时间 的 key 集合也是一个字典。zset 集合中存储 value 和 score 值的映射关系也是通过 dict 结 构实现的。

```
struct RedisDb {
 dict* dict; // all keys key=>value
 dict* expires; // all expired keys key=>long(timestamp)
 ...
}

struct zset {
 dict *dict; // all values value=>score
 zskiplist *zsl;
}
```

### dict内部结构

* dict 结构内部包含两个 hashtable，通常情况下只有一个 hashtable 是有值的。但是在 dict 扩容缩容时，需要分配新的 hashtable，然后进行渐进式搬迁，这时候两个 hashtable 存 储的分别是旧的 hashtable 和新的 hashtable。待搬迁结束后，旧的 hashtable 被删除，新的 hashtable 取而代之。

![image-20210908095607314](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210908095607314.png)

* hashtable的结构：第一维是数组，第二维是链表。数组中存储的是第二维链表的第一个元素的指针。

![image-20210908095751350](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210908095751350.png)

```
struct dictEntry {
 void* key;
 void* val;
 dictEntry* next; // 链接下一个 entry
}
struct dictht {
 dictEntry** table; // 二维
 long size; // 第一维数组的长度
 long used; // hash 表中的元素个数
 ...
}
```

### 渐进式rehash

* 搬迁操作埋伏在当前字典的后续指令中(来自客户端的 hset/hdel 指令等)，在客户端闲下来后，Redis 还会在定时任务中对字典进行主动搬迁。

### 查找过程

* 先通过hash_func，将key映射为一个整数，不同的key会被映射成比较均匀散乱的整数。只有hash值均匀了，整个hashtable才是平衡的，所有的二 维链表的长度就不会差距很远，查找算法的性能也就比较稳定。

### hash函数

* Redis 的字典默认的 hash 函数是 siphash。siphash 算法即使在输入 key 很小的情况下，也可以产生随机性特别好的输出，而 且它的性能也非常突出。

### hash攻击

* 如果 hash 函数存在偏向性，黑客就可能利用这种偏向性对服务器进行攻击。存在偏向 性的 hash 函数在特定模式下的输入会导致 hash 第二维链表长度极为不均匀，甚至所有的 元素都集中到个别链表中，直接导致查找效率急剧下降，从 O(1)退化到 O(n)。有限的服务器 计算能力将会被 hashtable 的查找效率彻底拖垮。这就是所谓 hash 攻击。

### 扩容条件

* 正常情况下，当 hash 表中元素的个数等于第一维数组的长度时，就会开始扩容，扩容 的新数组是原数组大小的 2 倍。不过如果 Redis 正在做 bgsave，为了减少内存页的过多分 离 (Copy On Write)，Redis 尽量不去扩容 (dict_can_resize)，但是如果 hash 表已经非常满 了，元素的个数已经达到了第一维数组长度的 5 倍 (dict_force_resize_ratio)，说明 hash 表 已经过于拥挤了，这个时候就会强制扩容。

### 缩容条件

* 当 hash 表因为元素的逐渐删除变得越来越稀疏时，Redis 会对 hash 表进行缩容来减少 hash 表的第一维数组空间占用。缩容的条件是元素个数低于数组长度的 10%。缩容不会考 虑 Redis 是否正在做 bgsave。

### set的结构

* Redis 里面 set 的结构底层实现也是字典，只不过所有的 value 都是 NULL，其它的特 性和字典一模一样。