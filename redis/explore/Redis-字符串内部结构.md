## Redis-字符串内部结构

* Redis的字符串叫做SDS，也就是Simple Dynamic String。它的结构是一个**带长度信息的字节数组**。

```
struct SDS<T> {
 T capacity; // 数组容量
 T len; // 数组长度
 byte flags; // 特殊标识位，不理睬它
 byte[] content; // 数组内容
}
```

* SDS 结构使用了范型 T，为什么不直接用 int 呢，这是因为当字符串比较短 时，len 和 capacity 可以使用 byte 和 short 来表示，Redis 为了对内存做极致的优化，不同 长度的字符串使用不同的结构体来表示。
* Redis 规定字符串的长度不得超过 512M 字节。创建字符串时 len 和 capacity 一样 长，不会多分配冗余空间，这是因为绝大多数场景下我们不会使用 append 操作来修改字符 串。

### embstr 和raw

* Redis 的字符串有两种存储方式，在长度特别短时，使用 emb 形式存储 (embeded)，当 长度超过 44 时，使用 raw 形式存储。
* Redis 对象头结构体，所有的 Redis 对象都有 下面的这个结构头：

```
struct RedisObject {
 int4 type; // 4bits
 int4 encoding; // 4bits
 int24 lru; // 24bits
 int32 refcount; // 4bytes
 void *ptr; // 8bytes，64-bit system
} robj;
```

* 不同的对象具有不同的类型 type(4bit)，同一个类型的 type 会有不同的存储形式 encoding(4bit)，为了记录对象的 LRU 信息，使用了 24 个 bit 来记录 LRU 信息。每个对 象都有个引用计数，当引用计数为零时，对象就会被销毁，内存被回收。ptr 指针将指向对 象内容 (body) 的具体存储位置。这样一个 RedisObject 对象头需要占据 **16 字节**的存储空 间。
* 在字符串比较小时，SDS 对象头的大小是 capacity+3，至少是 3。意味着分配一个字符串的最小空间占用为 19 字节 (16+3)。

```
struct SDS {
 int8 capacity; // 1byte
 int8 len; // 1byte
 int8 flags; // 1byte
 byte[] content; // 内联数组，长度为 capacity
}
```

![image-20210907202940175](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210907202940175.png)

* ，embstr 存储形式是这样一种存储形式，它将 RedisObject 对象头和 SDS 对 象连续存在一起，使用 malloc 方法一次分配。而 raw 存储形式不一样，它需要两次 malloc，两个对象头在内存地址上一般是不连续的。
* 而内存分配器 jemalloc/tcmalloc 等分配内存大小的单位都是 2、4、8、16、32、64 等 等，为了能容纳一个完整的 embstr 对象，jemalloc 最少会分配 32 字节的空间，如果字符 串再稍微长一点，那就是 64 字节的空间。如果总体超出了 64 字节，Redis 认为它是一个 大字符串，不再使用 emdstr 形式存储，而该用 raw 形式。
* embstr 最大能容纳的字符串长度就是 44。
  * 留给 content 的长度最多只有 45(64-19) 字节了。字符串又是 以\0 结尾。

### 扩容策略

* 字符串在长度小于 1M 之前，扩容空间采用加倍策略，也就是保留 100% 的冗余空 间。当长度超过 1M 之后，为了避免加倍后的冗余空间过大而导致浪费，每次扩容只会多分 配 1M 大小的冗余空间。

