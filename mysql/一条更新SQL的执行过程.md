## 一条更新SQL的执行过程

1. 客户端发送请求到服务端，建立连接
2. 服务端先看下查询缓存，对于更新某张表的SQL，该表的所有查询缓存都失效
3. 通过解析器，进行语法分析，一些系统关键字校验，校验语法是否合规
4. 通过优化器进行SQL优化，比如怎么选择索引之类，然后生成执行计划
5. 执行引擎去存储引擎查询需要更新的数据
6. 存储引擎判断当前缓冲池中是否存在需要更新的数据，存在就直接返回，否则从磁盘加载数据
7. 执行引擎调用存储引擎API去更新数据
8. 存储引擎更新数据，同时写入undo_log、redo_log信息
9. 执行引擎写binlog，提交事务，流程结束

### redo_log

* **重做日志**，是InnoDB存储引擎特有的，**用于保证事务的原子性和持久性**
* 保存我们执行的更新语句的记录，如果服务器或者Mysql宕机，通过redo_log可以恢复更新的数据
* 更新数据先更新缓冲池中的数据，然后写redo_log，会先写redo_log_buffer
  * 刷盘策略控制的参数是：`innodb_flush_log_at_trx_commit`
  * 0代表提交事务时不会写入磁盘，这样的话性能当然最好，但是在Mysql宕机的情况会丢失上一秒的事务的数据
  * 1代表提交事务一定会进行一次刷盘，同步当然性能最差，但是也最安全
  * 2代表写入文件系统的缓存，不进行刷盘。这个选项性能略差于1，Mysql宕机的话对数据没有任何影响，只有在操作系统宕机才会丢失数据，这种情况下默认Mysql每秒会执行一次刷盘

### undo_log

* 事务回滚的时候，需要用到undo_log，它**保证了事务的一致性**
* 相当于逆向操作，比如`insert`一条数据，就对应生成`delete`，`update`语句则生成相反的更新语句，这样做到将数据修改回之前的状态

### binlog

* binlog称为二进制日志，记录了改变数据库的那些SQL语句

* 不同于`redo_log`是独属于存储引擎独有的东西，`binlog`则是Mysql本身产生的日志
* 不同于redo_log是物理日志，binlog和undo_log都属于逻辑日志
* 逻辑日志可以认为就是存储的SQL本身，而物理日志看看redo_log存储的是啥就知道了，关于page_id页ID，offset偏移量啊这些东西，记录的是对页的修改
* 物理日志可以保证幂等性，而逻辑日志则不一定能，除非本身SQL就是幂等的
* binlog刷盘由参数`sync_binlog`控制

### 事务

* 为了保证写redo_log和binlog的一致性，实际采用了二阶段提交的方式
* prepare阶段：根据`innodb_flush_log_at_trx_commit`设置的刷盘策略决定是否写入磁盘，标记为prepare状态
* commit阶段：写入binlog日志，事务标记为提交状态

![mysql一条update语句执行过程](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/mysql%E4%B8%80%E6%9D%A1update%E8%AF%AD%E5%8F%A5%E6%89%A7%E8%A1%8C%E8%BF%87%E7%A8%8B.jpg)

