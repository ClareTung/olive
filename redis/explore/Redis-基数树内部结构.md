## Redis-基数树内部结构

* Rax 是 Redis 内部比较特殊的一个数据结构，它是一个有序字典树 (基数树 Radix  Tree)，按照 key 的字典序排列，支持快速地定位、插入和删除操作。
* rax 跟 zset 的不同在于它是按照 key 进行排序的。

![image-20210909142701340](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909142701340.png)

### 应用

* 英文字典单词
* 公安人员档案，身份证和履历
* Radix tree 还可以应用于时间序列应用，key 为时间戳，value 为发生在具体时间的事件 内容
* Web 服务器的 Router 它也是一棵 radix tree。这棵树上挂满了 URL  规则，每个 URL 规则上都会附上一个请求处理器。当一个请求到来时，我们拿这个请求的 URL 沿着树进行遍历，找到相应的请求处理器来处理。因为 URL 中可能存在正则 pattern，而且同一层的节点对顺序没有要求，所以它不算是一棵严格的 radix tree

![image-20210909142918322](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909142918322.png)

* rax 是一棵比较特殊的 radix tree，它在结构上不是标准的 radix tree。如果一个中间节点 有多个子节点，那么路由键就只是一个字符。如果只有一个子节点，那么路由键就是一个字 符串。后者就是所谓的「压缩」形式，多个字符压在一起的字符串。比如前面的那棵字典树 在 Rax 算法中将呈现出如下结构：

![image-20210909143400084](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909143400084.png)

* 图中的深蓝色节点就是「压缩」节点。 接下来我们再细看 raxNode.data 里面存储的到底是什么东西，它是一个比较复杂的结 构，按照压缩与否分为两种结构 压缩结构 子节点如果只有一个，那就是压缩结构，data 字段如下伪代码所示：

![image-20210909143541033](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909143541033.png)

* 如果是叶子节点，child 字段就不存在。如果是无意义的中间节点 (isNull)，那么 value  字段就不存在。 非压缩节点 如果子节点有多个，那就不是压缩结构，存在多个路由键，一个键是一个字 符。

![image-20210909143605597](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210909143605597.png)