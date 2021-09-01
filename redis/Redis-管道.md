## Redis-管道

### Redis的消息交互

* 当使用客户端对Redis进行一次操作时

![image-20210901173136885](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210901173136885.png)

* 如果连续多条指令，会花费过个网络数据包来回的时间

![image-20210901173219046](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210901173219046.png)

* 管道操作

![image-20210901173310734](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210901173310734.png)

### 管道压力测试

* Redis 自带了一个压力测试工具 redis-benchmark，使用这个工具就可以进行管道测试。

````
root@d32b29f2c4fa:/data# redis-benchmark -t set -q
SET: 90744.10 requests per second

root@d32b29f2c4fa:/data# redis-benchmark -t set -P 50 -q
SET: 1670000.00 requests per second

root@d32b29f2c4fa:/data# redis-benchmark -t set -P 100 -q
SET: 1759649.12 requests per second

root@d32b29f2c4fa:/data# redis-benchmark -t set -P 200 -q
SET: 1810714.25 requests per second

root@d32b29f2c4fa:/data# redis-benchmark -t set -P 300 -q
SET: 1805357.12 requests per second

root@d32b29f2c4fa:/data# redis-server -v
Redis server v=6.0.15 sha=00000000:0 malloc=jemalloc-5.1.0 bits=64 build=e5c93986d3d039bb
````

* 管道选项-P 参数，它表示单个管道内并行的请求数量
* 提升P参数到300的时候，QPS已经上不去了，受到cpu处理能力的影响

### 深入理解管道的本质

![image-20210901173659879](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210901173659879.png)

* write 操作只负责将数据写到本地操作系统内核的发送缓冲然后就返回了。剩下的事交给操作系统 内核异步将数据送到目标机器。但是如果发送缓冲满了，那么就需要等待缓冲空出空闲空间 来，这个就是写操作 IO 操作的真正耗时。
* read 操作只负 责将数据从本地操作系统内核的接收缓冲中取出来就了事了。但是如果缓冲是空的，那么就 需要等待数据到来，这个就是读操作 IO 操作的真正耗时。
* 管道的本质，它并不是服务器的什么特性，而是客户端通过改变了读写的顺序 带来的性能的巨大提升。

