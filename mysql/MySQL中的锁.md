## MySQL中的锁

### MySQL实现的两种行级锁

* **共享锁**：允许事务读一行数据，一般记为S，也称为读锁
* **排他锁**：允许事务删除或者更新一行数据，一般记为X，也称为写锁

### MySQL的隔离级别

* **读未提交Read Uncommitted**：能读到其他事务还没有提交的数据，这种现象叫做**脏读**
* **读已提交Read Committed**：只会读取其他事务已经提交的数据，所以不会产生RC的脏读问题。所以又带来一个问题叫做**不可重复读**，一个事务中两次一样的SQL查询可能查到的结果不一样
* **可重复读Repeatable Read**：RR是Mysql的默认隔离级别，一个事务中两次SQL查询总是会查到一样的结果，不存在不可重复读的问题，但是还是会有**幻读**的问题
* **串行Serializable**：串行场景没有任何问题，完全串行化的操作，读加读锁，写加写锁

![image-20210914205042387](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210914205042387.png)

### RR隔离级别下的幻读问题

* 同一事务下，不同的时间点，同样的查询，得到不同的行记录的集合
* 如果说一个`select`执行了两次，但是第二次比第一次多出来行记录，这就是幻读。所、以，对于幻读来说那一定是新增插入的数据
* 比如说在一个事务内，先查询`select * from user where age=10 for update`，得到的结果是id为[1,2,3]的记录，再次执行查询，得到了结果为[1,2,3,4]的记录，这是幻读

### MVCC+Next Key Lock解决幻读问题

* MVCC下读分为两种：分别叫做**快照读**和**当前读**。

#### 快照读

* 简单的`select`查询，查询的都是快照版本，这个场景下因为都是基于MVCC来查询快照的某个版本，所以不会存在幻读的问题。
* 对于RC级别来说，因为每次查询都重新生成一个read view，也就是查询的都是最新的快照数据，所以会可能每次查询到不一样的数据，造成不可重复读
* 对于RR级别来说只有第一次的时候生成read view，查询的是事务开始的时候的快照数据，所以就不存在不可重复读的问题，当然就更不可能有幻读的问题了

#### 当前读

* 当前读指的是`lock in share mode`、`for update` 、`insert`、`update`、`delete`这些需要加锁的操作。
* 对于MVCC来说就是解决的快照读的场景，而对于当前读那么就是Next-Key Lock要解决的事情

### MySQL行锁的3种实现算法

#### Record Lock

* 行记录的锁，**实际上是对索引记录的锁定**

#### Gap Lock

* 间隙锁，**用于锁定索引之间的间隙，但不会包含记录本身**
* **间隙锁是可重复读RR隔离级别下特有的**，另外几种场景不会使用间隙锁
  * 事务隔离级别设置为读已提交RC ，这样肯定没有间隙锁了
  * `Innodb_locks_unsafe_for_binlog`设置为1
  * 另外一种情况适用于**主键索引或者唯一索引**的等值查询条件，比如`select * from user where id=1`，`id`是主键索引，这样只使用Record Lock就可以了，因为能唯一锁定一条记录，所以没有必要再加间隙锁了，这是锁降级的过程

#### Next-Key Lock

* 实际上相当于Record Lock+Gap Lock的组合
* 因为MySQL跟标准RR不一样，标准的Repeatable Reads确实存在幻读问题，**但InnoDB中的Repeatable Reads是通过next-key lock解决了RR的幻读问题的**
* 有了next-key lock，所以在需要加行锁的时候，会同时在索引的间隙中加锁，这就使得其他事务无法在这些间隙中插入记录，这就解决了幻读的问题

### Next-Key Lock到底是如何解决幻读的？

#### 初始化数据

```sql
CREATE TABLE `user` (
	`id` INT ( 10 ) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR ( 255 ) DEFAULT NULL,
	`age` INT ( 10 ) DEFAULT NULL,
	`city` VARCHAR ( 255 ) DEFAULT NULL,
	PRIMARY KEY ( `id` ),
	UNIQUE KEY `idx_name` ( `name` ) USING BTREE,
KEY `idx_age` ( `age` ) USING BTREE 
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (5, 'x', 5, 'hefei');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (10, 'a', 10, 'sahnghai');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (15, 'q', 15, 'huzhou');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (20, 'b', 20, 'wuhan');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (25, 'y', 25, 'shenzhen');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (30, 'c', 30, 'beijing');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `city`) VALUES (35, 'z', 35, 'hangzhou');
```

#### 没有索引

* 更新语句`update user set city='nanjing' where city='wuhan'`会发生什么
* 因为`city`是没有索引的，所以存储引擎只能给所有记录加上锁，然后把数据都返回给Server层，然后Server层把`city`改成`nanjing`，再更新数据。
* 首先Record Lock会锁住现有的7条记录，间隙锁则会对主键索引的间隙全部加上间隙锁。
* 更新的时候没有索引是非常可怕的一件事情，相当于把整个表都给锁了，那表都给锁了当然不存在幻读了

![69ECE94D-A004-4da4-8F88-A87A420D7E61](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/69ECE94D-A004-4da4-8F88-A87A420D7E61.png)

#### 普通索引

* `select * from user where age=20 for update`
* 因为`age`是一个普通索引，存储引擎根据条件过滤查到所有匹配`age=20`的记录，给他们加上写锁，间隙锁会加在(10,20)，(20,30)的区间上，因此现在无论怎样都无法插入`age=20`的记录了
* 为什么要锁定这两个区间？如果不锁定这两个区间的话，那么还能插入比如`id=11,age=20`或者`id=21,age=20`的记录，这样就存在幻读了

![57BFFBC7-7D30-4350-AD99-71AB66396BD2](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/57BFFBC7-7D30-4350-AD99-71AB66396BD2.png)

#### 唯一索引和主键索引

* `select * from user where name='b' for update`
* 如果是唯一索引或者主键索引的话，并且是等值查询，实际上会发生锁降级，降级为Record Lock，就不会有间隙锁了
* 因为主键或者唯一索引能保证值是唯一的，所以也就不需要再增加间隙锁了
* 唯一索引不光锁定唯一索引，还会锁定主键索引，主键索引的话只要索引主键索引就行了

![5B286CAC-8EDB-413d-9F86-B42A70AD1A55](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/5B286CAC-8EDB-413d-9F86-B42A70AD1A55.png)

### MySQL的加锁机制

* 原则 1：加锁的基本单位是 next-key lock。是一个前开后闭区间。
* 原则 2：查找过程中访问到的对象才会加锁。
* 优化 1：索引上的等值查询，给唯一索引加锁的时候，next-key lock 退化为行锁。
* 优化 2：索引上的等值查询，向右遍历时且最后一个值不满足等值条件的时候，next-key lock 退化为间隙锁。
* 一个 bug：唯一索引上的范围查询会访问到不满足条件的第一个值为止



![mysql锁](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/mysql%E9%94%81.png)





