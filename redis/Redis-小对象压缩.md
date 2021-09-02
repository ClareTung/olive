

Redis-小对象压缩

## 32bit

* Redis使用内存不超过4G，可以考虑使用32bit进行编译

### 小对象压缩存储（ziplist）

* Redis 的 ziplist 是一个紧凑的**字节数组**结构，每个元素之间都是紧挨着的。

![image-20210902093430391](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210902093430391.png)

* 如果它存储的是 hash 结构，那么 key 和 value 会作为两个 entry 相邻存在一起。

```
root@d32b29f2c4fa:/data# redis-cli
127.0.0.1:6379> hset hello a 1
(integer) 1
127.0.0.1:6379> hset hello b 2
(integer) 1
127.0.0.1:6379> hset hello c 3
(integer) 1
127.0.0.1:6379> object encoding hello
"ziplist"
```

* 如果它存储的是 zset，那么 value 和 score 会作为两个 entry 相邻存在一起。

```
127.0.0.1:6379> object encoding hello
"ziplist"
127.0.0.1:6379> zadd world 1 a
(integer) 1
127.0.0.1:6379> zadd world 2 b
(integer) 1
127.0.0.1:6379> zadd world 3 c
(integer) 1
127.0.0.1:6379> object encoding world
"ziplist"
```

* Redis 的 intset 是一个紧凑的整数数组结构，它用于存放元素都是整数的并且元素个数 较少的 set 集合。

```
127.0.0.1:6379> sadd a 1 2 3
(integer) 3
127.0.0.1:6379> object encoding a
"intset"
```

* 如果 set 里存储的是字符串，那么 sadd 立即升级为 hashtable 结构。

```
127.0.0.1:6379> sadd sa yes no
(integer) 2
127.0.0.1:6379> object encoding sa
"hashtable"
```

### 存储界限

* 当集合对象的元素不断增加，或者某个 value 值过大，这种小对象存储也会 被升级为标准结构。Redis 规定在小对象存储结构的限制条件如下：

```
hash-max-zipmap-entries 512 # hash 的元素个数超过 512 就必须用标准结构存储
hash-max-zipmap-value 64 # hash 的任意元素的 key/value 的长度超过 64 就必须用标准结构存储
list-max-ziplist-entries 512 # list 的元素个数超过 512 就必须用标准结构存储
list-max-ziplist-value 64 # list 的任意元素的长度超过 64 就必须用标准结构存储
zset-max-ziplist-entries 128 # zset 的元素个数超过 128 就必须用标准结构存储
zset-max-ziplist-value 64 # zset 的任意元素的长度超过 64 就必须用标准结构存储
set-max-intset-entries 512 # set 的整数元素个数超过 512 就必须用标准结构存储
```

### 内存回收机制

* Redis 并不总是可以将空闲内存立即归还给操作系统。
* 操作系统回收内存是以页为单位，如果这个页上只要有一个 key  还在使用，那么它就不能被回收。
* 如果你执行 flushdb，然后再观察内存会发现内存确实被回收了。原因是所有的 key 都干掉了，大部分之前使用的页面都完全干净了，会立即被操作系统回收。
* Redis 虽然无法保证立即回收已经删除的 key 的内存，但是它会重用那些尚未回收的空 闲内存。

### 内存分配算法

* 需要适当的算法划分内存页，需要考虑内存碎片，需要平衡性能和效率。
* Redis的内存分配是由第三方内存分配库去实现，目前 Redis 可以使用 jemalloc(facebook) 库来管理内 存，也可以切换到 tcmalloc(google)。因为 jemalloc 相比 tcmalloc 的性能要稍好一些，所以 Redis 默认使用了 jemalloc。

```
127.0.0.1:6379> info memory
# Memory
used_memory:917920
used_memory_human:896.41K
used_memory_rss:8265728
used_memory_rss_human:7.88M
used_memory_peak:3931736
used_memory_peak_human:3.75M
used_memory_peak_perc:23.35%
used_memory_overhead:874824
used_memory_startup:812936
used_memory_dataset:43096
used_memory_dataset_perc:41.05%
allocator_allocated:965456
allocator_active:1228800
allocator_resident:3907584
total_system_memory:13280108544
total_system_memory_human:12.37G
used_memory_lua:37888
used_memory_lua_human:37.00K
used_memory_scripts:0
used_memory_scripts_human:0B
number_of_cached_scripts:0
maxmemory:0
maxmemory_human:0B
maxmemory_policy:noeviction
allocator_frag_ratio:1.27
allocator_frag_bytes:263344
allocator_rss_ratio:3.18
allocator_rss_bytes:2678784
rss_overhead_ratio:2.12
rss_overhead_bytes:4358144
mem_fragmentation_ratio:9.43
mem_fragmentation_bytes:7388816
mem_not_counted_for_evict:0
mem_replication_backlog:0
mem_clients_slaves:0
mem_clients_normal:61504
mem_aof_buffer:0
mem_allocator:jemalloc-5.1.0
active_defrag_running:0
lazyfree_pending_objects:0
```

* 通过 info memory 指令可以看到 Redis 的 mem_allocator 使用了 jemalloc。

