[TOC]

# Redisson

* wiki文档：[链接地址](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95)

## Redisson实现分布式锁原理(项目：redis-distributed-lock)

### 高效分布式锁

* 互斥
  * 在分布式高并发的条件下，需要保证同一时刻只能有一个线程获得锁
* 防止死锁
  * 设置锁的有效时间，确保系统出现故障后，在一定时间内能够主动去释放锁
* 性能
  * 对于访问量大的共享资源，需要考虑减少锁的等待时间，避免造成大量线程阻塞
  * **锁的颗粒度要尽量小**。针对单个字段。
  * **锁的范围尽量小**。针对单条记录。
* 重入
  * 同一个线程可以重复拿到同一个资源的锁。
  * 重入锁有利于资源的高效利用。

### Redisson原理

#### ![redis-分布式锁](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/redis-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81.png)

#### 加锁机制

* 线程去获取锁，获取成功，执行lua脚本，保存数据到redis。
* 线程去获取锁，获取失败，一个while循环尝试获取锁，获取成功后，执行lua脚本，保存数据到redis。

#### watch dog自动延期机制

* 线程1业务没有执行完，时间过了，但是线程1还想持有锁，就会启动一个watch dog后台线程，不断的延长锁key的生存时间。

#### 为什么要用lua脚本

* 如果业务逻辑复杂，通过封装在lua脚本中发送给Redis，而且Redis是单线程的，这样就保证了这段复杂业务逻辑执行的**原子性**。

#### 可重入加锁机制

* 为什么呢？
  * Redis存储锁的数据类型是Hash类型
  * Hash数据类型的key值包含了当前线程信息
* 可重入机制最大的优点就是，相同线程不需要等待锁。

#### Redis分布式锁的缺点

* 在哨兵模式下，在当前master获得了锁，需要异步复制给对应的slave节点，如果master宕机，主备切换，slave节点变成master节点，新的线程过来在新的master上可能加锁成功，此时就会有多个客户端对同一个分布式锁完成了加锁。可能导致各种脏数据产生。

## RedissonLock和RLock

* Redisson分布式锁的实现是基于RLock接口，RedissonLock实现RLock接口。

### RLock锁API

* lock(long leaseTime, TimeUnit unit)：加锁，默认有效时间30秒
* unlock()：解锁
* tryLock(long leaseTime, TimeUnit unit)：尝试获取锁，如果成功，则返回true，如果失败，就返回false

## 延迟队列(项目：redis-delay-queue)

### 使用场景

* 下单成功，30分钟未支付。支付超时，自动取消订单
* 订单签收，签收后7天未进行评价。订单超时未评价，系统默认好评
* 下单成功，商家5分钟未接单，订单取消
* 配送超时，推送短信提醒

```txt
对于延时比较长的场景，实时性不高的场景，可以此采用任务调度的方式定时轮询处理。如：xxl-job。
Redis的延迟队列是一种比较简单、轻量级的方式。
更好的解决方案：使用消息中间件Kafka、RabbitMQ的延迟队列。
```







