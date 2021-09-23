## ElasticSearch搜索原理

### 倒排索引 (Inverted Index)

* 是存储文档内容到文档位置映射关系的数据库索引结构。
* 索引 Term 和倒排表 Posting List
* Term 对应的文档 ID 的列表就是 Posting List
* 一般 Term 都是按照顺序排序的，Term Dictionary 往往很大，无法完整放入内存，这是为了更快的查询，还需要再给它创建索引，也就是 Term Index
* Term Index 包含的是这些 Term 的前缀，可以进行快速定位到 Term Dictionary 的某一位置，然后再从这个位置向后查询

![es-搜索原理](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/es-%E6%90%9C%E7%B4%A2%E5%8E%9F%E7%90%86.png)