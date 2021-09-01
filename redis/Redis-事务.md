## Redis-事务

### Redis事务的基本使用

* multi/exec/discard。multi 指示事务的开始， exec 指示事务的执行，discard 指示事务的丢弃。

```
127.0.0.1:6379> multi
OK
127.0.0.1:6379> incr books
QUEUED
127.0.0.1:6379> incr books
QUEUED
127.0.0.1:6379> exec
1) (integer) 1
2) (integer) 2
```

* 所有的指令在 exec 之前不执行，而是缓存在 服务器的一个事务队列中，服务器一旦收到 exec 指令，才开执行整个事务队列，执行完毕 后一次性返回所有指令的运行结果。因为 Redis 的单线程特性，它不用担心自己在执行队列 的时候被其它指令打搅，可以保证他们能得到的「原子性」执行。

### 原子性

* 事务的原子性是指要么事务全部成功，要么全部失败。

```
127.0.0.1:6379> multi
OK
127.0.0.1:6379> set books clare
QUEUED
127.0.0.1:6379> incr books
QUEUED
127.0.0.1:6379> set poorman tung
QUEUED
127.0.0.1:6379> exec
1) OK
2) (error) ERR value is not an integer or out of range
3) OK
127.0.0.1:6379> get books
"clare"
127.0.0.1:6379> get poorman
"tung"
```

* 事务执行到中间遇到失败了，因为我们不能对一个字符串进行数学运算， 事务在遇到指令执行失败后，后面的指令还继续执行，所以 poorman 的值能继续得到设置。
* Redis 的事务根本不能算「原子性」，而仅仅是满足了事务的「隔 离性」，隔离性中的串行化——当前执行的事务有着不被其它事务打断的权利。

### discard（丢弃）

* 丢弃事务缓存队列中的所有指令

### 优化

* Redis事务务在发送每个指令到事务缓存队列时都要经过一次网络读写，当一个事 务内部的指令较多时，需要的网络 IO 时间也会线性增长。在客户端执行事务时结合pipeline一起使用，这样可将多次IO操作压缩为单次IO操作。

```
pipe = redis.pipeline(transaction=true)
pipe.multi()
pipe.incr("books")
pipe.incr("books")
values = pipe.execute()
```

### Watch

* Redis 提供了这种 watch 的机制，它就是一种乐观锁。有了 watch 我们又多了一种可以 用来解决并发修改的方法。
* watch 会在事务开始之前盯住 1 个或多个关键变量，当事务执行时，也就是服务器收到 了 exec 指令要顺序执行缓存的事务队列时，Redis 会检查关键变量自 watch 之后，是否被 修改了 (包括当前事务所在的客户端)。如果关键变量被人动过了，exec 指令就会返回 null  回复告知客户端事务执行失败，这个时候客户端一般会选择重试。

```
127.0.0.1:6379> watch books
OK
127.0.0.1:6379> incr books
(integer) 6
127.0.0.1:6379> multi
OK
127.0.0.1:6379> incr books
QUEUED
127.0.0.1:6379> exec
(nil)
```

