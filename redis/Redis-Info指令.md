## Redis-Info指令

* Info指令能够显示的信息非常多：
  * Server服务器运行的环境参数
  * Clients客户端相关消息
  * Memory服务器运行内存统计数据
  * Persistence持久化信息
  * Stats通用统计数据
  * Replication主从复制相关消息
  * CPU使用情况
  * Cluster集群信息
  * KeySpace键值对统计数量信息

### Redis每秒执行多少次指令

```
redis-cli info stats|grep ops
```

### Redis连接了多少客户端

```
root@d32b29f2c4fa:/data# redis-cli info clients
# Clients
connected_clients:6  # 这个就是正在连接的客户端数量
client_recent_max_input_buffer:8
client_recent_max_output_buffer:0
blocked_clients:0
tracking_clients:0
clients_in_timeout_table:0
```

### Redis内存占用多大

```
root@d32b29f2c4fa:/data# redis-cli info memory|grep used|grep human
used_memory_human:958.88K       # 内存分配器 (jemalloc) 从操作系统分配的内存总量
used_memory_rss_human:7.90M     # 操作系统看到的内存占用 ,top 命令看到的内存
used_memory_peak_human:3.75M    # Redis 内存消耗的峰值 
used_memory_lua_human:37.00K    # lua 脚本引擎占用的内存大小
used_memory_scripts_human:0B
```

* 如果单个 Redis 内存占用过大，并且在业务上没有太多压缩的空间的话，可以考虑集群 化了。

### 复制积压缓冲区多大

* 复制积压缓冲区大小非常重要，它严重影响到主从复制的效率。当从库因为网络原因临 时断开了主库的复制，然后网络恢复了，又重新连上的时候，这段断开的时间内发生在 master 上的修改操作指令都会放在积压缓冲区中，这样从库可以通过积压缓冲区恢复中断的 主从同步过程。
* 积压缓冲区是环形的，后来的指令会覆盖掉前面的内容。如果从库断开的时间过长，或 者缓冲区的大小设置的太小，都会导致从库无法快速恢复中断的主从同步过程，因为中间的 修改指令被覆盖掉了。这时候从库就会进行全量同步模式，非常耗费 CPU 和网络资源。
* 如果有多个从库复制，积压缓冲区是共享的，它不会因为从库过多而线性增长。如果实 例的修改指令请求很频繁，那就把积压缓冲区调大一些，几十个 M 大小差不多了，如果很 闲，那就设置为几个 M。

```
root@d32b29f2c4fa:/data# redis-cli info replication|grep backlog
repl_backlog_active:0
repl_backlog_size:1048576         # 这个就是积压缓冲区大小
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

