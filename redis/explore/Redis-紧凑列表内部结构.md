## Redis-紧凑列表内部结构

* Redis 5.0 又引入了一个新的数据结构 listpack，它是对 ziplist 结构的改进，在存储空间 上会更加节省，而且结构上也比 ziplist 要精简。

```
struct listpack<T> {
 int32 total_bytes; // 占用的总字节数
 int16 size; // 元素个数
 T[] entries; // 紧凑排列的元素列表
 int8 end; // 同 zlend 一样，恒为 0xFF
}
```

![image-20210909135313704](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909135313704.png)

* listpack 跟 ziplist 的结构几乎一摸一样，只是少了一个 zltail_offset 字段。 ziplist 通过这个字段来定位出最后一个元素的位置，用于逆序遍历。不过 listpack 可以通过 其它方式来定位出最后一个元素的位置，所以 zltail_offset 字段就省掉了

```
struct lpentry {
 int<var> encoding;
 optional byte[] content;
 int<var> length;
}
```

* listpack 的设计的目的是用来取代 ziplist，不过当下还没有做好替换 ziplist 的准备，它目前只使用在了新增加的 Stream 数据结构中。

