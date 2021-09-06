## Redis-Stream

* Redis5.0的新特性增加了一个数据结构Stream，它是一个新的强大的支持多播的可持续化的消息队列，借鉴了Kafka的设计。

![image-20210902181907216](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210902181907216.png)

* Redis Stream 的结构如上图所示，它有一个消息链表，将所有加入的消息都串起来，每 个消息都有一个唯一的 ID 和对应的内容。消息是持久化的，Redis 重启后，内容还在。
* 每个 Stream 都有唯一的名称，它就是 Redis 的 key，在我们首次使用 xadd 指令追加消 息时自动创建。
* 每个 Stream 都可以挂多个消费组，每个消费组会有个游标 last_delivered_id 在 Stream  数组之上往前移动，表示当前消费组已经消费到哪条消息了。每个消费组都有一个 Stream  内唯一的名称，消费组不会自动创建，它需要单独的指令 xgroup create 进行创建，需要指定 从 Stream 的某个消息 ID 开始消费，这个 ID 用来初始化 last_delivered_id 变量。
* 每个消费组 (Consumer Group) 的状态都是独立的，相互不受影响。也就是说同一份 Stream 内部的消息会被每个消费组都消费到
* 同一个消费组 (Consumer Group) 可以挂接多个消费者 (Consumer)，这些消费者之间是 竞争关系，任意一个消费者读取了消息都会使游标 last_delivered_id 往前移动。每个消费者有 一个组内唯一名称。
* 消费者 (Consumer) 内部会有个状态变量 pending_ids，它记录了当前已经被客户端读取 的消息，但是还没有 ack。如果客户端没有 ack，这个变量里面的消息 ID 会越来越多，一 旦某个消息被 ack，它就开始减少。这个 pending_ids 变量在 Redis 官方被称之为 PEL，也 就是 Pending Entries List，这是一个很核心的数据结构，它用来确保客户端至少消费了消息一 次，而不会在网络传输的中途丢失了没处理。

### 消息ID

* 消息 ID 的形式是 timestampInMillis-sequence，例如 1527846880572-5，它表示当前的消 息在毫米时间戳 1527846880572 时产生，并且是该毫秒内产生的第 5 条消息。消息 ID 可以 由服务器自动生成，也可以由客户端自己指定，但是形式必须是整数-整数，而且必须是后面 加入的消息的 ID 要大于前面的消息 ID。

### 消息内容

* 消息内容就是键值对，形如 hash 结构的键值对，这没什么特别之处。

### 增删改查

1. xadd 追加消息 
2. xdel 删除消息，这里的删除仅仅是设置了标志位，不影响消息总长度 
3. xrange 获取消息列表，会自动过滤已经删除的消息 
4. xlen 消息长度 
5. del 删除 Stream

```
# * 号表示服务器自动生成 ID，后面顺序跟着一堆 key/value
# name 名称，price 价格
127.0.0.1:6379> xadd food * name apple price 10
"1630579240733-0"
127.0.0.1:6379> xadd food * name rice price 2
"1630579273515-0"
127.0.0.1:6379> xadd food * name pear price 8
"1630579285637-0"
127.0.0.1:6379> xlen food
(integer) 3
# -表示最小值 , + 表示最大值
127.0.0.1:6379> xrange food - +
1) 1) "1630579240733-0"
   2) 1) "name"
      2) "apple"
      3) "price"
      4) "10"
2) 1) "1630579273515-0"
   2) 1) "name"
      2) "rice"
      3) "price"
      4) "2"
3) 1) "1630579285637-0"
   2) 1) "name"
      2) "pear"
      3) "price"
      4) "8"
# 指定最大消息 ID 的列表
127.0.0.1:6379> xrange food - 1630579273515-0
1) 1) "1630579240733-0"
   2) 1) "name"
      2) "apple"
      3) "price"
      4) "10"
2) 1) "1630579273515-0"
   2) 1) "name"
      2) "rice"
      3) "price"
      4) "2"
127.0.0.1:6379> xdel food 1630579273515-0
(integer) 1
127.0.0.1:6379> xlen food
(integer) 2
127.0.0.1:6379> xdel food 1630579285637-0
(integer) 1
127.0.0.1:6379> xlen food
(integer) 1
# 删除整个 Stream
127.0.0.1:6379> del food
(integer) 1
```

### 独立消费

* 我们可以在不定义消费组的情况下进行 Stream 消息的独立消费，当 Stream 没有新消 息时，甚至可以阻塞等待。Redis 设计了一个单独的消费指令 xread，可以将 Stream 当成普 通的消息队列 (list) 来使用。使用 xread 时，我们可以完全忽略消费组 (Consumer Group)  的存在，就好比 Stream 就是一个普通的列表 (list)。

```
127.0.0.1:6379> xread count 2 streams food 0-0
1) 1) "food"
   2) 1) 1) "1630582340455-0"
         2) 1) "name"
            2) "pear"
            3) "price"
            4) "8"
      2) 1) "1630582353237-0"
         2) 1) "name"
            2) "apple"
            3) "price"
            4) "10"
            
# 从尾部读取一条消息
127.0.0.1:6379> xread count 1 streams food $
(nil)
# 从尾部阻塞等待新消息到来，下面的指令会堵住，直到新消息到来
127.0.0.1:6379> xread block 0 count 1 streams food $
```

### 创建消费组

![image-20210902195705220](C:\Users\EDZ\AppData\Roaming\Typora\typora-user-images\image-20210902195705220.png)

* Stream 通过 xgroup create 指令创建消费组 (Consumer Group)，需要传递起始消息 ID 参数用 来初始化 last_delivered_id 变量。

```
# 表示从头开始消费
127.0.0.1:6379> xgroup create food cg1 0-0
OK
# $ 表示从尾部开始消费，只接受消息，当前Stream消息会全部忽略
127.0.0.1:6379> xgroup create food cg2 $
OK
# 获取Stream信息
127.0.0.1:6379> xinfo stream food
 1) "length"
 2) (integer) 3                   # 共3个消息
 3) "radix-tree-keys"
 4) (integer) 1
 5) "radix-tree-nodes"
 6) (integer) 2
 7) "last-generated-id"
 8) "1630582365173-0"
 9) "groups"
10) (integer) 2                   # 两个消费组
11) "first-entry"                 # 第一个消息
12) 1) "1630582340455-0"
    2) 1) "name"
       2) "pear"
       3) "price"
       4) "8"
13) "last-entry"                  # 最后一个消息
14) 1) "1630582365173-0"
    2) 1) "name"
       2) "rice"
       3) "price"
       4) "2"
# 获取Stream的消费组信息
127.0.0.1:6379> xinfo groups food
1) 1) "name"
   2) "cg1"
   3) "consumers"
   4) (integer) 0                 # 该消费组还没有消费者
   5) "pending"
   6) (integer) 0                 # 该消费组没有正在处理的消息
   7) "last-delivered-id"
   8) "0-0"
2) 1) "name"
   2) "cg2"
   3) "consumers"
   4) (integer) 0
   5) "pending"
   6) (integer) 0
   7) "last-delivered-id"
   8) "1630582365173-0"
```

### 消费

* Stream 提供了 xreadgroup 指令可以进行消费组的组内消费，需要提供消费组名称、消 费者名称和起始消息 ID。它同 xread 一样，也可以阻塞等待新消息。读到新消息后，对应的消息 ID 就会进入消费者的 PEL(正在处理的消息) 结构里，客户端处理完毕后使用 xack  指令通知服务器，本条消息已经处理完毕，该消息 ID 就会从 PEL 中移除。

```
# > 号表示从当前消费组的 last_delivered_id 后面开始读
# 每当消费者读取一条消息，last_delivered_id 变量就会前进
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 1 streams food >
1) 1) "food"
   2) 1) 1) "1630582340455-0"
         2) 1) "name"
            2) "pear"
            3) "price"
            4) "8"
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 1 streams food >
1) 1) "food"
   2) 1) 1) "1630582353237-0"
         2) 1) "name"
            2) "apple"
            3) "price"
            4) "10"
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 2 streams food >
1) 1) "food"
   2) 1) 1) "1630582365173-0"
         2) 1) "name"
            2) "rice"
            3) "price"
            4) "2"
# 再继续读取，就没有新消息了
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 1 streams food >
(nil)
# 阻塞等待
127.0.0.1:6379> xreadgroup GROUP cg1 c1 block 100 count 1 streams food >
(nil)
127.0.0.1:6379> xinfo groups food
1) 1) "name"
   2) "cg1"
   3) "consumers"
   4) (integer) 1    # 一个消费者
   5) "pending"
   6) (integer) 3    # 共3条正在处理的消息还没有ack
   7) "last-delivered-id"
   8) "1630582365173-0"
2) 1) "name"
   2) "cg2"
   3) "consumers"
   4) (integer) 0
   5) "pending"
   6) (integer) 0
   7) "last-delivered-id"
   8) "1630582365173-0"
127.0.0.1:6379> xinfo consumers food cg1
1) 1) "name"
   2) "c1"
   3) "pending"
   4) (integer) 3
   5) "idle"
   6) (integer) 103566
# ack一条消息
127.0.0.1:6379> xack food cg1 1630582340455-0
(integer) 1
127.0.0.1:6379> xinfo consumers food cg1
1) 1) "name"
   2) "c1"
   3) "pending"
   4) (integer) 2  # 变成2个没有确认了
   5) "idle"
   6) (integer) 185398
```

### Stream消息太多怎么办？

* xadd提供一个指定长度maxlen，可以将老的消息干掉，确保最多不超过指定长度

```
127.0.0.1:6379> xlen food
(integer) 3
127.0.0.1:6379> xadd food maxlen 3 * name peach price 3
"1630585068296-0"
127.0.0.1:6379> xlen food
(integer) 3
```

### 消息如果忘记 ACK 会怎样?

* Stream 在每个消费者结构中保存了正在处理中的消息 ID 列表 PEL，如果消费者收到 了消息处理完了但是没有回复 ack，就会导致 PEL 列表不断增长，如果有很多消费组的 话，那么这个 PEL 占用的内存就会放大

![image-20210902202009180](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210902202009180.png)

### PEL 如何避免消息丢失

* 在客户端消费者读取 Stream 消息时，Redis 服务器将消息回复给客户端的过程中，客 户端突然断开了连接，消息就丢失了。但是 PEL 里已经保存了发出去的消息 ID。待客户端 重新连上之后，可以再次收到 PEL 中的消息 ID 列表。不过此时 xreadgroup 的起始消息 ID 不能为参数>，而必须是任意有效的消息 ID，一般将参数设为 0-0，表示读取所有的 PEL 消息以及自 last_delivered_id 之后的新消息。

### 小结

* Stream 的消费模型借鉴了 Kafka 的消费分组的概念，它弥补了 Redis Pub/Sub 不能持 久化消息的缺陷。但是它又不同于 kafka，Kafka 的消息可以分 partition，而 Stream 不行。