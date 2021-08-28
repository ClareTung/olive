## Redis分布式锁

### 并发问题

* 原子操作：指不会被线程调度机制打断的操作，这种操作一旦开始，就一直运行到结束，中间不会有任何context switch线程切换。
* 一个操作要修改用户状态，修改用户状态要先读出用户状态，在内存中进行修改，改了再存回去。如果操作同时进行，就会出现并发问题，因为读取和保存状态这两个操作不是原子的。

![image-20210828113130242](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210828113130242.png)

### 分布式锁

* 获取锁：setnx lock:o1 true
  * setnx：set if not exists
* 过期时间：expire lock:o1 5
* 释放锁：del lock:o1

![image-20210727094150768](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210727094150768.png)

* setnx和expire是两条指令，不是原子指令，服务进程挂掉，可能导致死锁

* set 指令的扩展参数解决非原子操作的问题
  * set lock:o1 true ex 5 nx

### 超时问题

* Redis分布式锁不能解决超时问题，在加锁和释放锁之间的逻辑执行时间太长，**超出了锁的超时限制**（此时其他线程就能获取到锁，执行完释放锁，接下来的线程又可以拿到锁），就会出现问题。
* 一个方案：为set指令的value参数设置一个随机数，释放锁时先匹配随机数是否一致，然后在删除key。依靠Lua脚本，Lua脚本可以保证连续多个指令的原子性执行。

```lua
# delifequals
if redis.call("get",KEYS[1]) == ARGV[1] then
 return redis.call("del",KEYS[1])
else
 return 0
end 
```

### 可重入性

* 可重入性是指线程在持有锁的情况下再次请求加锁，如果一个锁支持同一个线程的多次加锁，那么这个锁就是可重入的。

