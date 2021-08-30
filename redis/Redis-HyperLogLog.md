## Redis-HyperLogLog

* HyperLogLog 提供不精确的去重计数方案，虽然不精确但是也不是非常不 精确，标准误差是 0.81%，这样的精确度已经可以满足UV 统计需求了。
* pfadd：增加计数
* pfcount：获取计数
* pfmerge：将多个pf计数值累加在一起形成一个新的pf值

* HyperLogLog这个数据结构需要占据12K的存储空间，不适合统计单个用户相关的数据
* 存储空间会由稀疏矩阵存储，变成稠密矩阵，才会占用12K的空间

### 实现原理

![image-20210828170535245](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210828170535245.png)

### pf的内存占用为什么是12K

* Redis 的 HyperLogLog  实现中用到的是 16384 个桶，也就是 2^14，每个桶的 maxbits 需要 6 个 bits 来存储，最 大可以表示 maxbits=63，于是总共占用内存就是 2^14 * 6 / 8 = 12k 字节。