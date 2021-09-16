## MySQL索引下推

### 什么是索引下推

* 索引下推(Index Condition Pushdown) ICP 是Mysql5.6之后新增的功能，主要核心点在于把数据筛选的过程放在了存储引擎层去处理，而不是像之前一样放到Server层去做过滤

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL DEFAULT NULL,
  `age` int(10) NULL DEFAULT NULL,
  `city` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_age_name`(`age`,`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (5, 'x', 5, 'hefei');
INSERT INTO `user` VALUES (10, 'a', 10, 'sahnghai');
INSERT INTO `user` VALUES (15, 'q', 15, 'huzhou');
INSERT INTO `user` VALUES (20, 'b', 20, 'wuhan');
INSERT INTO `user` VALUES (25, 'y', 25, 'shenzhen');
INSERT INTO `user` VALUES (30, 'c', 30, 'beijing');
INSERT INTO `user` VALUES (35, 'z', 35, 'hangzhou');
```

* 对`user`表的联合索引`idx_age_name`，执行查询`explain SELECT * from user where age >10 and name = 'a'`，Extra`中显示了`Using index condition，表示出现了索引下推。

![image-20210916102854896](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210916102854896.png)

* 按照我们上述的场景，实际上就存在两个索引树，一个是主键索引，存储了具体的数据的信息，另外则是`age_name`的联合索引，保存了主键的ID。

![20210916103246](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/20210916103246.jpg)

```html
在没有ICP索引下推的时候，这个查询的流程应该是这样（略过无关的细节）：

Mysql Server层调用API查询存储引擎数据
存储引擎根据联合索引首先通过条件找到所有age>10的数据
找到的每一条数据都根据主键索引进行回表查询，直到找到不符合条件的结果
返回数据给Server层，Server根据条件对结果进行过滤，流程结束
而有了ICP之后的流程则是这样：

Mysql Server层调用API查询存储引擎数据
存储引擎根据联合索引首先通过条件找到所有age>10的数据，根据联合索引中已经存在的name数据进行过滤，找到符合条件的数据
根据找到符合条件的数据，回表查询
返回数据给Server层，流程结束
```

* 使用ICP之后我们就是简单的通过联合索引中本来就有的数据直接过滤了，不需要再查到一堆无用的数据去Server层进行过滤，这样的话减少了回表的次数和返回的数据，IO次数减少了，对性能有很好的提升。

#### ICP使用场景限制

1. 首先，ICP适用于range、ref、eq_ref和ref_or_null的场景下
2. InnoDB和MyISAM都支持ICP，Mysql partition分表的话也可以使用
3. 对于InndoDB而言，ICP只支持二级索引，因为主键索引它用不上不是吗？
4. 子查询不支持

* 5.6以上的版本了，默认就是开启ICP的，想关闭的话可以通过命令`SET optimizer_switch = 'index_condition_pushdown=off';`

### 一个误区

* 一般来说，正常情况下Mysql一次查询都只能走一个索引

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL DEFAULT NULL,
  `age` int(10) NULL DEFAULT NULL,
  `city` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_age`(`age`) USING BTREE,
  KEY `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (5, 'x', 5, 'hefei');
INSERT INTO `user` VALUES (10, 'a', 10, 'sahnghai');
INSERT INTO `user` VALUES (15, 'q', 15, 'huzhou');
INSERT INTO `user` VALUES (20, 'b', 20, 'wuhan');
INSERT INTO `user` VALUES (25, 'y', 25, 'shenzhen');
INSERT INTO `user` VALUES (30, 'c', 30, 'beijing');
INSERT INTO `user` VALUES (35, 'z', 35, 'hangzhou');

explain SELECT * from user where age >10 and name like 'a%';
```

![image-20210916103855737](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210916103855737.png)

* 这里`Using index condition`并不代表一定是使用了索引下推，只是代表可以使用，但是不一定用了

* 索引下推一定是在联合索引的情况下，根据联合索引本身就有的数据直接做一次过滤，而不用再进行多次无用的回表再到Server层进行过滤

