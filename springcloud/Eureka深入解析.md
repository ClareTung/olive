[TOC]

## Eureka深入解析

### 组件调用关系

#### 服务提供者

* 启动后，向注册中心发起register请求，注册服务；
* 在运行过程中，定时向注册中心发送renew心跳，证明服务依然健康；
* 停止服务提供者，向注册中心发起cancel请求，清空当前服务注册信息。

#### 服务消费者

* 启动后，从注册中心拉取服务注册信息；
* 在运行过程中，定时更新服务注册信息；

#### 注册中心

* 启动后，从其他节点拉取服务注册信息；
* 运行过程中，定时运行evict任务，剔除没有按时renew的服务（包括非正常停止和网络故障的任务）；
* 运行过程中，接收到的register、renew和cancel请求，都会同步至其它注册中心节点。

### 数据存储结构

![ba23ed453a521a76fc0b587b8a0e9321](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/ba23ed453a521a76fc0b587b8a0e9321.png)

* Eureka的数据存储分为两层：数据存储层和缓存层。
* Eureka Client在拉取服务信息时，先从缓存层获取，如果获取不到，先把数据存储层的数据加载到缓存中，再从缓存中获取。值得注意的是，数据存储层的数据结构是服务信息，而缓存中保存的是经过处理加工过的、可直接传输到Eureka Client的数据结构。

#### 数据存储层

* 数据存储层的registry是一个双层的ConcurrentHashMap，数据存储在内存中。
  * 第一层的key是spring.application.name，value是第二层的ConcurrenthashMap；
  * 第二层的ConcurrentHashMap的key是服务的InstanceId，value是Lease对象；
  * Lease对象包含了服务详情和服务治理的相关属性。

#### 二级缓存

* Eureka实现了二级缓存来保存对外传输的服务信息。

**一级缓存（readOnlyCacheMap）：**

* ConcurrentHashMap<Key, Value> readOnlyCacheMap，无过期时间，保存服务信息的对外输出数据结构。

**二级缓存（readWriteCacheMap）：**

* Loading<Key, Value> readWriteCacheMap，本质上是Google的guava缓存，包含失效机制，保存服务信息的对外输出数据结构。

#### 缓存更新机制

**删除二级缓存：**

1. Eureka Client发送register、renew和cancel请求并更新registry注册表之后，删除二级缓存；
2. Eureka Server自身的Evict Task剔除服务后，删除二级缓存；
3. 二级缓存本身设置了guava的失效机制，隔一段时间后自己自动失效；

**加载二级缓存：**

1. Eureka Client发送getRegistry请求后，如果二级缓存中没有，就触发guava的load，即从registry中获取原始服务信息后进行处理加工，再加载到二级缓存中。
2. Eureka Server更新一级缓存的时候，如果二级缓存没有数据，也会触发guava的load。

**更新一级缓存：**

1. Eureka Server内置了一个TimerTask，定时将二级缓存中的数据同步到一级缓存（这个动作包括了删除和加载）。

### 服务注册机制

* 服务提供者、服务消费者、以及服务注册中心自己，启动后都会向注册中心注册服务。
* 注册中心服务接收到register请求后：
  1. 保存服务信息，将服务信息保存到registry中；
  2. 更新队列，将此事件添加到更新队列中，供Eureka Client增量同步服务信息使用。
  3. 清空二级缓存，即readWriteCacheMap，用于保证数据的一致性。
  4. 更新阈值，供剔除服务使用。
  5. 同步服务信息，将此事件同步至其他的Eureka Server节点。

### 服务续约机制

* 服务注册后，要定时（默认30S，可自己配置）向注册中心发送续约请求，告诉注册中心“我还活着”。
* 注册中心收到续约请求后：
  1. 更新服务对象的最近续约时间，即Lease对象的lastUpdateTimestamp;
  2. 同步服务信息，将此事件同步至其他的Eureka Server节点。

* 注：剔除服务之前会先判断服务是否已经过期，判断服务是否过期的条件之一是**续约时间**和当前时间的差值是不是大于阈值。

### 服务注销机制

* 服务**正常停止**之前会向注册中心发送注销请求，告诉注册中心“我要下线了”。
* 注册中心服务接收到cancel请求后：
  1. 删除服务信息，将服务信息从registry中删除；
  2. 更新队列，将此事件添加到更新队列中，供Eureka Client增量同步服务信息使用。
  3. 清空二级缓存，即readWriteCacheMap，用于保证数据的一致性。
  4. 更新阈值，供剔除服务使用。
  5. 同步服务信息，将此事件同步至其他的Eureka Server节点。

* 注：服务正常停止才会发送Cancel，如果是非正常停止，则不会发送，此服务由Eureka Server主动剔除。

### 服务剔除机制

* Eureka Server提供了服务剔除的机制，用于剔除没有正常下线的服务。
* 服务的剔除包括三个步骤，首先判断是否满足服务剔除的条件，然后找出过期的服务，最后执行剔除。

#### 判断是否满足服务剔除的条件

* 有两种情况可以满足服务剔除的条件：
  1. 关闭了自我保护
  2. 如果开启了自我保护，需要进一步判断是Eureka Server出了问题，还是Eureka Client出了问题，如果是Eureka Client出了问题则进行剔除。
* **Eureka自我保护阈值是区分Eureka Client还是Eureka Server出问题的临界值：如果超出阈值就表示大量服务可用，少量服务不可用，则判定是Eureka Client出了问题。如果未超出阈值就表示大量服务不可用，则判定是Eureka Server出了问题**。

##### 自我保护阈值

* **阈值的计算公式如下：**

  - 自我保护阈值 = 服务总数 * 每分钟续约数 * 自我保护阈值因子。
  - 每分钟续约数 =（60S/客户端续约间隔）
  - 最后自我保护阈值的计算公式为：**自我保护阈值 = 服务总数 \* （60S/客户端续约间隔） \* 自我保护阈值因子。**

* **举例**：

  如果有100个服务，续约间隔是30S，自我保护阈值0.85。

  自我保护阈值=100 * 60 / 30 * 0.85 = 170。

  如果上一分钟的续约数=180>170，则说明大量服务可用，是服务问题，进入剔除流程；

  如果上一分钟的续约数=150<170，则说明大量服务不可用，是注册中心自己的问题，进入自我保护模式，不进入剔除流程。

#### 找出过期服务

* 遍历所有的服务，判断上次续约时间距离当前时间大于阈值就标记为过期。并将这些过期的服务保存到集合中。

#### 剔除服务

* 在剔除服务之前先计算剔除的数量，然后遍历过期服务，通过洗牌算法确保每次都公平的选择出要剔除的任务，最后进行剔除。执行剔除服务后：
  1. 删除服务信息，从registry中删除服务。
  2. 更新队列，将当前剔除事件保存到更新队列中。
  3. 清空二级缓存，保证数据的一致性。

### 服务获取机制

* Eureka Client获取服务有两种方式，全量同步和增量同步。获取流程是根据Eureka Server的多层数据结构进行的：

![8baa1ca6feef4bb0f080d0c4ce1c560a](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/8baa1ca6feef4bb0f080d0c4ce1c560a.png)

* 无论是全量同步还是增量同步，都是先从缓存中获取，如果缓存中没有，则**先加载到缓存中，再从缓存中获取。（registry只保存数据结构，缓存中保存ready的服务信息。）**

#### 从一级缓存中获取

1. 先判断是否开启了一级缓存
2. 如果开启了则从一级缓存中获取，如果存在则返回，如果没有，则从二级缓存中获取
3. 如果未开启，则跳过一级缓存，从二级缓存中获取

####  从二级缓存中获取

1. 如果二级缓存中存在，则直接返回；
2. 如果二级缓存中不存在，则先将数据加载到二级缓存中，再从二级缓存中获取。**注意加载时需要判断是增量同步还是全量同步，增量同步从recentlyChangedQueue中load，全量同步从registry中load。**

### 服务同步机制

* 服务同步机制是用来同步Eureka Server节点之间服务信息的。它包括Eureka Server启动时的同步，和运行过程中的同步

#### 启动时同步

* Eureka Server启动后，遍历eurekaClient.getApplications获取服务信息，并将服务信息注册到自己的registry中。

  注：这里是两层循环，第一层循环是为了保证已经拉取到服务信息，第二层循环是遍历拉取到的服务信息。

#### 运行过程中同步

![49a95f7398fe945ba96a5febc190787d](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/49a95f7398fe945ba96a5febc190787d.png)

* 当Eureka Server节点有register、renew、cancel请求进来时，会将这个请求封装成TaskHolder放到acceptorQueue队列中，然后经过一系列的处理，放到batchWorkQueue中
* TaskExecutor.BatchWorkerRunnable是个线程池，不断的从batchWorkQueue队列中poll出TaskHolder，然后向其他Eureka Server节点发送同步请求。

* **这里省略了两个部分：**
  - 一个是在acceptorQueue向batchWorkQueue转化时，省略了中间的processingOrder和pendingTasks过程。
  - 另一个是当同步失败时，会将失败的TaskHolder保存到reprocessQueue中，重试处理。

### ZK和Eureka

* ZK 的设计原则是 CP，即强一致性和分区容错性。他保证数据的强一致性，但舍弃了可用性，**如果出现网络问题可能会影响 ZK 的选举，导致 ZK 注册中心的不可用**。
* Eureka 的设计原则是 AP，即可用性和分区容错性。他保证了注册中心的可用性，但舍弃了数据一致性，**各节点上的数据有可能是不一致的（会最终一致）**。

![image-20210928170846463](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210928170846463.png)