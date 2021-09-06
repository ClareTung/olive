## Redis-限流

* 限流可以控制流量，还可以控制用户行为，避免垃圾请求。

### 简单限流

* 滑动时间窗口，使用zset数据结构，score值圈出来这个时间窗口，只需保留这个时间窗口，窗口之外的数据都不需要。value使用秒时间戳即可。

![image-20210830103513028](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210830103513028.png)

* 用一个 zset 结构记录用户的行为历史，每一个行为都会作为 zset 中的一个 key 保存下来。同一个用户同一种行为用一个 zset 记录。

### 漏斗（funnel）限流

![image-20210830105000037](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210830105000037.png)

* 漏洞的容量是有限的，如果将漏嘴堵住，然后一直往里面灌水，它就会变满，直至再也 装不进去。如果将漏嘴放开，水就会往下流，流走一部分之后，就又可以继续往里面灌水。 如果漏嘴流水的速率大于灌水的速率，那么漏斗永远都装不满。如果漏嘴流水速率小于灌水 的速率，那么一旦漏斗满了，灌水就需要暂停并等待漏斗腾空。

### Redis-Cell

* Redis 4.0 提供了一个限流 Redis 模块，它叫 redis-cell。
* 该模块也使用了漏斗算法，并 提供了原子的限流指令。

```
hp笔记本:0>cl.throttle clare:reply 15 30 60 1
```

* key：clare:reply
* 15：capacity 漏斗容量
* 30 operations / 60 seconds 漏水速率
* need 1 quota（可选参数，默认值也是1）