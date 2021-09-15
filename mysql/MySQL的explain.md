[TOC]

## MySQL的explain

### id

* 查询编号
* 没有子查询或者联合查询的话，就只有一条
* 如果有子查询的话，会出现一条id为null的记录，并且标志查询结果，union结果会放到临时表中，这里的表名就是<union1,2>这种格式

![20210915225557](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915225557.jpg)

### select_type

* 关联类型，决定访问表的方式

#### SIMPLE

* 简单查询，代表没有子查询或者union

#### PRIMARY

* 如果不是简单查询，那么最外层查询就会被标记成PRIMARY

#### UNION和UNION RESULT

* 包含联合查询，第一个被标记成了`PRIMARY`，union之后的查询被标记成`UNION`，以及最后产生的`UNION RESULT`

#### DERIVED

* 用来标记出现在from里的子查询，这个结果会放入临时表中，也叫做派生表

![20210915225746](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915225746.jpg)

#### SUBQUERY

* 不在from里的子查询

![20210915225923](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915225923.jpg)

#### DEPENDENT

* 代表关联子查询（子查询使用了外部查询包含的列），和`UNION`，`SUBQUERY`组合产生不同的结果

![20210915230043](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915230043.jpg)

#### UNCACHEABLE

* 代表不能缓存的子查询，也可以和`UNION`，`SUBQUERY`组合产生不同的结果

![20210915230152](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915230152.jpg)

#### MATERIALIZED

* 物化子查询是Mysql对子查询的优化，第一次执行子查询时会将结果保存到临时表，物化子查询只需要执行一次
* 比如上述DERIVED就是物化的一种体现，与之对应的就是DEPENDENT，每次子查询都需要重新调用

### table

* 显示表名

### partitions

* 数据的分区信息，没有分区忽略就好了

### type

* 关联类型，决定通过什么方式找到每一行数据

#### **system&const**

* 这通常是最快的查找方式，代表Mysql通过优化最终转换成常量查询，最常规的做法就是直接通过主键或者唯一索引查询

![20210915231221](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915231221.jpg)

* system是const的一个特例（只有一行数据的系统表），随便找一张系统表，就插入一条数据就可以看到system了

#### **eq_ref**

* 通常通过主键索引或者唯一索引查询时会看到eq_ref，它最多只返回一条数据。`user_id`是唯一索引，为了测试就关联以下主键索引

![20210915231527](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915231527.jpg)

#### ref

* 也是通过索引查找，但是和eq_ref不同，ref可能匹配到多条符合条件的数据，比如最左前缀匹配或者不是主键和唯一索引
* 最简单的办法，随便查一个普通索引就可以看到

![20210915231756](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915231756.jpg)

#### **fulltext**

* 使用FULLTEXT索引

#### **ref_or_null**

* 和ref类似，但是还要进行一次查询找到NULL的数据
* 相当于是对于IS NULL查询的优化，如果表数据量太少的话，你或许能看到这里类型是全表扫描

![20210915232210](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915232210.jpg)

#### **index_merge**

* 索引合并是在Mysql5.1之后引入的，就像下面的一个OR查询，按照原来的想法要么用name的索引，要么就是用age的索引
* 对于这种单表查询（无法跨表合并）用到了多个索引的情况，每个索引都可能返回一个结果，Mysql会对结果进行取并集、交集，这就是索引合并了

![20210915232519](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915232519.jpg)

#### **unique_subquery**

* unique_subquery只是eq_ref的一个特例，对于下图中这种`in`的语句查询会出现以提高查询效率

![20210915232713](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915232713.jpg)

#### **index_subquery**

* 和unique_subquery类似，只是针对的是非唯一索引

![20210915232812](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915232812.jpg)

#### **range**

* 范围查询，其实就是带有限制条件的索引扫描
* 常见的范围查询比如`between and`，>，<，like，in 都有可能出现range

#### **index**

* 跟全表扫描类似，只是扫表是按照索引顺序进行

#### ALL

* 全表扫描

### possible_keys

* 可以使用哪些索引

### key

* 实际决定使用哪个索引

### key_len

* 索引字段的可能最大长度，不是表中实际数据使用的长度

### ref

* 表示key展示的索引实际使用的列或者常量

### rows

* 查询数据需要读取的行数，只是一个预估的数值，但是能很直观的看出SQL的优劣

### filtered

* 5.1版本之后新增字段，表示针对符合查询条件的记录数的百分比估算，用rows和filtered相乘可以计算出关联表的行数

### Extra

#### **Using index**

* 使用覆盖索引

#### **Using index condition**

* 可以使用索引下推(不一定真的使用了)，索引下推简单来说就是加上了条件筛选，减少了回表的操作

![20210915233650](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915233650.jpg)

#### **Using temporary**

* 排序使用了临时表

#### **Using filesort**

* 使用外部索引文件排序，但是不能从这里看出是内存还是磁盘排序，我们只能知道更消耗性能

#### **Using where**

* where过滤

#### **Zero limit**

* 除非你写个LIMIT 0

#### **Using sort_union(), Using union(), sing intersect()**

* 使用了索引合并

![20210915233852](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210915233852.jpg)