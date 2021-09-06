## Redis-LRU

* 当 Redis 内存超出物理内存限制时，内存的数据会开始和磁盘产生频繁的交换 (swap)。
* 在生产环境中我们是不允许 Redis 出现交换行为的，为了限制最大使用内存，Redis 提 供了配置参数 maxmemory 来限制内存超出期望大小。
* 当实际内存超出 maxmemory 时，Redis 提供了几种可选策略 (maxmemory-policy) ：
  * noeviction：不会继续服务写请求 (DEL 请求可以继续服务)，读请求可以继续进行。
  * volatile-lru：尝试淘汰设置了过期时间的 key，最少使用的 key 优先被淘汰。
  *  volatile-ttl：key 的剩余寿命 ttl 的值，ttl  越小越优先被淘汰。
  * volatile-random：过淘汰的 key 是过期 key 集合中随机的 key。
  * allkeys-lru：区别于 volatile-lru，这个策略要淘汰的 key 对象是全体的 key 集合，而不 只是过期的 key 集合。这意味着没有设置过期时间的 key 也会被淘汰。
  * allkeys-random：不过淘汰的策略是随机的 key。
  * volatile-xxx： 策略只会针对带过期时间的 key 进行淘汰，allkeys-xxx 策略会对所有的 key 进行淘汰。如果你只是拿 Redis 做缓存，那应该使用 allkeys-xxx，客户端写缓存时 不必携带过期时间。

### LRU算法

* 实现 LRU 算法除了需要 key/value 字典外，还需要附加一个链表，链表中的元素按照 一定的顺序进行排列。当空间满的时候，会踢掉链表尾部的元素。当字典的某个元素被访问 时，它在链表中的位置会被移动到表头。所以链表的元素排列顺序就是元素最近被访问的时 间顺序。
* 位于链表尾部的元素就是不被重用的元素，所以会被踢掉。位于表头的元素就是最近刚 刚被人用过的元素，所以暂时不会被踢。

### 近似LRU算法

* Redis不使用LRU算法，是因为需要消耗大量的内存。
* 使用近似LRU算法，给每个key增加了一个额外的小字段，这个字段长度是24个bit，也就是最后一次被访问的时间戳。
* Redis采用随机采样淘汰5（默认）个key，如果是 allkeys 就是从所有的 key 字典中 随机，如果是 volatile 就从带过期时间的 key 字典中随机。
* 采样数量越大，近似 LRU 算法的效果越接近严格 LRU  算法。

