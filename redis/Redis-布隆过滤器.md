## Redis-布隆过滤器

### 布隆过滤器是什么

* 布隆过滤器可以理解为一个不怎么精确的set结构，使用contains方法判断某个对象是否存在时，它可能误判。
* 当布隆过滤器说某个值不存在时，肯定就不存在；当它说存在时，这个值可能不存在。

### Redis中布隆过滤器的基本使用

* Redis4.0提供了插件功能，布隆过滤器作为一个插件到RedisServer中，给Redis提供了强大的布隆去重功能

```
> root@DESKTOP-48RTCQO:/# docker pull redislabs/rebloom
> root@DESKTOP-48RTCQO:/# docker run -p6380:6379 redislabs/rebloom
> redis-cli
```

#### 报错

```
WARNING overcommit_memory is set to 0! Background save may fail under low memory condition. To fix this issue add 'vm.overcommit_memory = 1' to /etc/sysctl.conf and then reboot or run the command 'sysctl vm.overcommit_memory=1' for this to take effect.


1:M 28 Aug 2021 10:02:49.872 # WARNING you have Transparent Huge Pages (THP) support enabled in your kernel. This will create latency and memory usage issues with Redis. To fix this issue run the command 'echo madvise > /sys/kernel/mm/transparent_hugepage/enabled' as root, and add it to your /etc/rc.local in order to retain the setting after a reboot. Redis must be restarted after THP is disabled (set to 'madvise' or 'never').
```

```
root@DESKTOP-48RTCQO:/mnt/c/Users/EDZ# vi /etc/sysctl.conf
root@DESKTOP-48RTCQO:/mnt/c/Users/EDZ# vi /etc/rc.local
```

![image-20210828182431639](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210828182431639.png)

* bf.add：添加元素
* bf.exists：查询元素是否存在
* bf.madd：同时添加多个元素
* bf.mexists：同时判断多个元素是否存在

```
127.0.0.1:6379> bf.add bloom u1
(integer) 1
127.0.0.1:6379> bf.add bloom u2
(integer) 1
127.0.0.1:6379> bf.exists bloom u1
(integer) 1
127.0.0.1:6379> bf.exists bloom u3
(integer) 0
127.0.0.1:6379> bf.madd bloom u4 u5 u6
1) (integer) 1
2) (integer) 1
3) (integer) 1
127.0.0.1:6379> bf.mexists bloom u3 u4 u5
1) (integer) 0
2) (integer) 1
3) (integer) 1
```

### 布隆过滤器的原理

![image-20210828183314607](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210828183314607.png)

* 每个布隆过滤器对应到 Redis 的数据结构里面就是一个大型的位数组和几个不一样的无 偏 hash 函数。所谓无偏就是能够把元素的 hash 值算得比较均匀。
* 向布隆过滤器中添加 key 时，会使用多个 hash 函数对 key 进行 hash 算得一个整数索 引值然后对位数组长度进行取模运算得到一个位置，每个 hash 函数都会算得一个不同的位 置。再把位数组的这几个位置都置为 1 就完成了 add 操作。
* 向布隆过滤器询问 key 是否存在时，跟 add 一样，也会把 hash 的几个位置都算出 来，看看位数组中这几个位置是否都位 1，只要有一个位为 0，那么说明布隆过滤器中这个 key 不存在。如果都是 1，这并不能说明这个 key 就一定存在，只是极有可能存在，因为这 些位被置为 1 可能是因为其它的 key 存在所致。

### 空间占用估计

* 专门的计算网站：https://krisives.github.io/bloom-calculator/

### 布隆过滤器应用

* 推荐系统去重
* 爬虫url去重
* NoSQL查询
* 邮箱系统的垃圾邮件过滤