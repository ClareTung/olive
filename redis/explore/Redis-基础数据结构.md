## Redis基础数据结构

* string (字符串)、list (列表)、set (集合)、hash (哈希) 和 zset (有序集合)。

### string（字符串）

* Redis所有的数据结构都是以唯一的key字符串作为名称，然后通过这个唯一key值来获取相应的value数据。类似Java语言的ArrayList。
* 不同的数据结构的差异就在于value的结构不一样。
* Redis 的字符串是动态字符串，是可以修改的字符串。
* 采用预分配冗余空间的方式来减少内存的频繁分配。实际分配的空间 capacity 一般要高于实际字符串长度 len。
* 当字符串长度小于 1M 时，扩容都是加倍现有的空间，如果超过 1M，扩容时一次只会多扩 1M 的空间。需要注意的是字符串最大长度为 512M。
* 常用命令
  * set name hello
  * get name 
  * mset n1 boy n2 girl
  * mget n1 n2
  *  setex name 5 codehole # 5s 后过期，等价于 set+expire
  * setnx name codehole # 如果 name 不存在就执行 set 创建
  * incr age  # 整数自增
  * incrby age 5 

### list（列表）

* 相当于Java语言的LinkedList，它是链表而不是数组。
* 常用命令
  *  rpush books python java golang 

### hash（字典）

* Redis 的字典相当于 Java 语言里面的 HashMap，它是无序字典。
* 数组 + 链表二维结构。
* 常用命令
  * hset books java "think in java" # 命令行的字符串如果包含空格，要用引号括起来
  * hgetall books # entries()，key 和 value 间隔出现
  *  hget books java 

### set（集合）

* Redis 的集合相当于 Java 语言里面的 HashSet，它内部的键值对是无序的唯一的。
* 常用命令
  * sadd books python 
  * smembers books # 注意顺序，和插入的并不一致，因为 set 是无序的

### zset（有序列表）

* 它类似于 Java 的 SortedSet 和 HashMap 的结合体。
* 一方面它是一个 set，保证了内部value 的唯一性，另一方面它可以给每个 value 赋予一个 score，代表这个 value 的排序权重。
* 内部实现使用跳跃列表的数据结构。
* 常用命令
  * zadd books 9.0 "think in java" 

