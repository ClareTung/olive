[toc]

# 轻量级流程编排引擎LiteFlow

[liteflow官方网站](https://liteflow.yomahub.com/)

## 规则文件

* 规则由Node节点和Chain节点组成，在Springboot和Spring环境中Node节点非必须
* xml格式
  * 本地文件：flow.xml
  * zk方式
  * 自定义配置源
* json格式
* yml格式

## 用代码动态构造规则

* 构造模式和配置模式可以结合使用，配置模式的底层也使用了构造模式

### 构造Node

* @Componet/@LiteflowComponet：组件会被自动扫描注册
* 动态代理类、脚本节点，动态构建就很有意义

### 构建一个Chain

* 

## 使用指南

### 开启和关闭

```
liteflow.enable=false
```

### 同步和异步编排

* 同步流程块：表示abcd4个组件会挨个同步执行

```xml
<then value="a,b,c,d">
```

* 异步流程块：表示abcd4个组件并行执行

```xml
<when value="a,b,c,d">
```

* 异步线程池配置
* 异步并行组

```xml
<when group="g1" value="a,b"/>
<when group="g2" value="c,d"/>
```

### 执行器

* FlowExecutor：用来执行一个流程
* 返回LiteflowResponse
* 返回类型是Slot接口的子类
* 返回类型为Future，2.6.13开始

### 数据槽

* 执行器执行流程时会分配唯一一个数据槽给这个请求，不同请求的数据槽是完全隔离的。请求的上下文，存放此请求的所有数据，不同组件之间不传递参数，所有的数据交互都是通过这个数据槽来实现的
* 默认Slot实现，DefaultSlot：Map存储
* 自定义Slot只要继承AbsSlot即可
* Slot数量：默认是1024，容量不足会按照0.75因子去自动扩容

### 普通组件

* 组件节点需要继承NodeComponent类，实现process方法
* isAccess判断是否进入此节点
* Slot获取：getSlot获取当前的数据槽，从而可以获取任何数据

### 条件组件

* 动态根据业务逻辑判断到底执行哪一个节点
* 实现方法processCond，返回的就是要具体执行的节点

### 脚本组件

* 

### 声明式组件

* 不用继承任何类和实现任何接口，普通类依靠注解来完成LiteFlow组件的声明
* 实现是依靠动态代理，底层使用的是ByteBuddy

### 前置后置组件

* 

### 组件标签

* tag用来给组件做个标记，执行中可以获取到

### 子流程

* 

### 隐式子流程

* 

### 私有投递

* 可以给指定的组件投递1或多个参数

### 组件重试

* 

### 组件切面

* 

### 异常处理机制

* 

### 步骤打印

* 

### 不同规则加载

* 

### when异步线程池

* 

### 自定义组件执行器

* 

### 简单监控

* 

