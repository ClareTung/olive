## Redis的过期策略、内存淘汰机制及LRU算法实现

### 过期策略

#### 定期删除

* Redis默认每隔100ms就随机抽取一些设置了过期时间的key，检查其是否过期，如果过期就删除。
* cpu负载会很高，都消耗在了检查过期key上了
* 可能会导致很多过期的key到了时间并没有被删除掉

#### 惰性删除

* 在获取某个key的时候，Redis会检查一下，这个key设置的过期时间有没有过期，过期了就删除，不返回任何东西
* 同样还是可能漏掉很多key，堆积在内存中，导致Redis内存耗尽

### 内存淘汰机制

* Redis 内存淘汰机制有以下几个：
  - noeviction: 当内存不足以容纳新写入数据时，新写入操作会报错，这个一般没人用吧，实在是太恶心了。
  - **allkeys-lru**：当内存不足以容纳新写入数据时，在**键空间**中，移除最近最少使用的 key（这个是**最常用**的）。
  - allkeys-random：当内存不足以容纳新写入数据时，在**键空间**中，随机移除某个 key，这个一般没人用吧，为啥要随机，肯定是把最近最少使用的 key 给干掉啊。
  - volatile-lru：当内存不足以容纳新写入数据时，在**设置了过期时间的键空间**中，移除最近最少使用的 key（这个一般不太合适）。
  - volatile-random：当内存不足以容纳新写入数据时，在**设置了过期时间的键空间**中，**随机移除**某个 key。
  - volatile-ttl：当内存不足以容纳新写入数据时，在**设置了过期时间的键空间**中，有**更早过期时间**的 key 优先移除。

### LRU算法

* LRU就是Least Recently Used，最近最少使用。LRU 算法会将最近最少用的缓存移除，让给最新使用的缓存。

```java
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int capacity;

    /**
     * 传递进来最多能缓存多少数据
     *
     * @param capacity 缓存大小
     */
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * 如果map中的数据量大于设定的最大容量，返回true，再新加入对象时删除最老的数据
     *
     * @param eldest 最老的数据项
     * @return true则移除最老的数据
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 当 map中的数据量大于指定的缓存个数的时候，自动移除最老的数据
        return size() > capacity;
    }
}
```



