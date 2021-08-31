## tcp连接优化

* socket连接数足够，但是并没有全部被用上，猜测，每次请求过后，tcp连接并没有立即被释放，导致socket无法重用。
* tcp链接在经过四次握手结束连接后并不会立即释放，而是处于timewait状态，会等待一段时间，以防止客户端后续的数据未被接收。

```
#timewait 的数量，默认是 180000。
net.ipv4.tcp_max_tw_buckets = 6000

net.ipv4.ip_local_port_range = 1024 65000

#启用 timewait 快速回收。
net.ipv4.tcp_tw_recycle = 1

#开启重用。允许将 TIME-WAIT sockets 重新用于新的 TCP 连接。
net.ipv4.tcp_tw_reuse = 1
```



