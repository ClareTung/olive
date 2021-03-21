# RabbitMQ

[TOC]

## windows安装

### 安装Erlang

* 下载：http://erlang.org/download/otp_win64_23.2.exe
* 添加环境变量
  * ERLANG_HOME
    * C:\Program Files\erl-23.2
  * Path
    * %ERLANG_HOME%\bin
  * cmd -> erl 验证

## RabbitMQ安装

* 下载：https://www.rabbitmq.com/install-windows.html
* 安装完成后：C:\Program Files\RabbitMQ Server\rabbitmq_server-3.8.11\sbin>rabbitmq-plugins.bat enable rabbitmq_management
* 访问：http://localhost:15672/
  * guest/guest

## RabbitMQ 核心概念

* 参考：https://www.rabbitmq.com/amqp-0-9-1-quickref.html
* https://www.rabbitmq.com/tutorials/amqp-concepts.html

![817174355](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/817174355.jpg)

### Broker

* 消息队列服务器的实体，类似于 JMS 规范中的 JMS provider。它**用于接收和分发消息**，有时候也称为 Message Broker 或者更直白的称为 RabbitMQ Server。

### Virtual Host

* 和 Web 服务器中的虚拟主机（Virtual Host）是类似的概念，出于**多租户和安全因素设计的**

* 可以将 RabbitMQ Server **划分成多个独立的空间，彼此之间互相独立**，这样就可以将一个 RabbitMQ Server 同时**提供给多个用户使用**，每个用户在自己的空间内创建 Exchange 和 Queue

### Exchange

* **交换机用于接收消息**
* 消息到达 Broker 的第一站，然后根据**交换机的类型和路由规则**（Routing Key），将消息分发到特定的**队列**中去。常用的交换机类型有：direct (point-to-point)、topic (publish-subscribe) 和 fanout (multicast)

### Queue

* **生产者发送的消息就是存储在这里**
* 在 AMQP 中，消息会经过 Exchange，由 Exchange 来将消息分发到各个队列中
* 消费者可以直接从这里取走消息

### Binding

* 绑定的作用就是把 **Exchange** 和 Queue 按**照路由规则**绑定起来，路由规则可由下面的 **Routing Key** 指定

### Routing Key

* 路由关键字，**Exchange 根据这个关键字进行消息投递**

### Producer/Publisher

* 消息生产者或发布者，产生消息的程序

### Consumer/Subscriber

* 消息消费者或订阅者，接收消息的程序

### Connection

* 生产者和消费者和 Broker 之间的连接，一个 Connection 实际上就对应着一条 TCP 连接

### Channel

* 由于 TCP 连接的创建和关闭开销非常大，如果每次访问 Broker 都建立一个 Connection，在消息量大的时候效率会非常低。Channel 是在 Connection 内部建立的逻辑连接，相当于一次会话，如果应用程序支持多线程，通常每个线程都会创建一个单独的 Channel 进行通讯，各个 Channel 之间完全隔离，但这些 Channel 可以公用一个 Connection。

## RabbitMQ 实战

* 官方实践的例子：https://www.rabbitmq.com/getstarted.html

### Hello World

![2016-02-18_56c53cbca169c](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/2016-02-18_56c53cbca169c.jpg)

* 队列是有Channel声明的，而且这个操作是[幂等](http://baike.baidu.com/view/2067025.htm)的。同名的队列多次声明也只会创建一次。

### 工作队列

![2016-02-18_56c53cbccaaab](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/2016-02-18_56c53cbccaaab.jpg)

* 工作队列（又名：任务队列）的主要任务是为了避免立即做一个资源密集型的却又必须等待完成的任务。
* 将任务封装为消息并发给队列。在后台运行的工作者（consumer）将其取出，然后最终执行。当你运行多个工作者（consumer），队列中的任务被工作进行共享执行。

#### Round-robin dispatching（轮询分发）

* 在默认情况下，RabbitMQ将逐个发送消息到在序列中的下一个消费者(而不考虑每个任务的时长等等，且是提前一次性分配，并非一个一个分配)。平均每个消费者获得相同数量的消息

#### Fair dispatch（公平分发）

* 如果所有的工作者都处于繁忙状态，你的队列有可能被填充满。你可能会观察队列的使用情况，然后增加工作者，或者使用别的什么策略
* 使用公平分发，必须关闭自动应答，改为手动应答

* 消息持久化

`boolean durable = true;
channel.queueDeclare("hello", durable, false, false, null);`

### 发布订阅

![python-three-overall](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/python-three-overall.png)

#### Exchanges（交换机）

* 类型有：Direct、Topic、Headers和Fanout。fanout它是广播接受到的消息给所有的队列

`channel.exchangeDeclare("logs", "fanout");`

#### Nameless exchange（匿名交换）

`channel.basicPublish("", TASK_QUEUE_NAME,
        MessageProperties.PERSISTENT_TEXT_PLAIN,
        message.getBytes("UTF-8"));`

* 第一个参数为空就是匿名的交换机

#### Temporary queues（临时队列）

`String queueName = channel.queueDeclare().getQueue();`

* 提供一个无参的queueDeclare()方法，创建一个非持久化、独立的、自动删除的队列，且名字是随机生成的
* 一旦消费者断开连接，队列将自动删除

![image-20210207172929388](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210207172929388.png)

#### Bindings（绑定）

`channel.queueBind(queueName, EXCHANGE_NAME, "");`

* 关联交换机和队列
* 第三个参数表示：**binding key参数**。
  * 绑定键关键取决于交换机的类型。对于fanout类型，忽略此参数。

### 路由转发

#### Direct exchange（直接转发）

![2016-02-18_56c53cbdb84fc](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/2016-02-18_56c53cbdb84fc.jpg)

* 消息会被推送至绑定键（binding key）和消息发布附带的选择键（routing key）完全匹配的队列

#### Multiple bindings（多重绑定）

![2016-02-18_56c53cbdc6130](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/2016-02-18_56c53cbdc6130.jpg)

* 使用一个绑定键绑定多个队列是完全合法的

### 主题交换机

#### Topic exchange

* 发送给主题交换机的消息不能是任意设置的选择键，必须是用小数点隔开的一系列的标识符。
* 标识符不超过上限的255个字节
* 绑定键也必须以相同的格式。主题交换机的逻辑类似于direct类型的交换机。消息通过一个特定的路由键发送到所有与绑定键匹配的队列中
* 关于绑定键有两种特殊的情况：*（星号）可以代替任意一个标识符 ；#（井号）可以代替零个或多个标识符

![2016-02-18_56c53cbe063d0](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/2016-02-18_56c53cbe063d0.jpg)

### 远程过程调用RPC

* 将使用RabbitMQ构建一个RPC系统：一个客户端和一个可扩展的RPC服务器端

### PublisherConfirms 

* ### Enabling Publisher Confirms on a Channel

  * ```
    ch.confirmSelect();
    ```



