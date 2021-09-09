## Redis-快速列表内部结构

* Redis中list列表数据结构是quicklist。

```
127.0.0.1:6379> rpush code java go py
(integer) 3
127.0.0.1:6379> debug object code
Value at:0x7f686671c830 refcount:1 encoding:quicklist serializedlength:17 lru:3766215 lru_seconds_idle:120 ql_nodes:1 ql_avg_node:1.00 ql_ziplist_max:-2 ql_compressed:0 ql_uncompressed_size:15
```

* quicklist 是 ziplist 和 linkedlist 的混合体，它 将 linkedlist 按段切分，每一段使用 ziplist 来紧凑存储，多个 ziplist 之间使用双向指针串 接起来。

![image-20210909110326100](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909110326100.png)

```
struct ziplist {
 ...
}
struct ziplist_compressed {
 int32 size;
 byte[] compressed_data;
}
struct quicklistNode {
 quicklistNode* prev;
 quicklistNode* next;
 ziplist* zl; // 指向压缩列表
 int32 size; // ziplist 的字节总数
 int16 count; // ziplist 中的元素数量
 int2 encoding; // 存储形式 2bit，原生字节数组还是 LZF 压缩存储
 ...
}
struct quicklist {
 quicklistNode* head;
 quicklistNode* tail;
 long count; // 元素总数
 int nodes; // ziplist 节点的个数
 int compressDepth; // LZF 算法压缩深度
 ...
}
```

* 为了进一步节约空间，Redis 还会对 ziplist 进行压缩存储，使用 LZF 算法压缩，可以选择压缩深度。

### 每个ziplist存多少元素

* quicklist 内部默认单个 ziplist 长度为 8k 字节，超出了这个字节数，就会新起一个 ziplist。ziplist 的长度由配置参数 list-max-ziplist-size 决定。

### 压缩深度

![image-20210909111927629](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909111927629.png)

* quicklist 默认的压缩深度是 0，也就是不压缩。压缩的实际深度由配置参数 listcompress-depth 决定。为了支持快速的 push/pop 操作，quicklist 的首尾两个 ziplist 不压 缩，此时深度就是 1。如果深度为 2，就表示 quicklist 的首尾第一个 ziplist 以及首尾第二 个 ziplist 都不压缩。