# Netty

## Netty是什么

* Java的开源项目

* Netty是一个**异步**的、**基于事件驱动**的**网络**应用框架，用以开发高性能、高可靠的网络IO程序。
* 主要针对TCP协议下，面向Clients端的高并发应用。
* 或者是大量的数据持续传输。
* 是一个NIO框架。

## Netty的应用场景

* 远程服务调用RPC框架
* 提供TCP/UDP和Http协议栈
* 高性能通信和序列化组件（AVRO）的RPC框架

## IO模型

* 用什么样的通道进行数据的发送和接收
* Java支持3种网络IO模型：BIO、NIO、AIO
* BIO：同步阻塞IO，服务器为一个连接一个线程。客户端有连接请求，服务器就需要启动一个线程进行处理。适用于连接数且固定的架构。
* NIO：同步非阻塞，服务器模式为一个线程处理多个请求，Selector(选择器)。适用于连接数多且连接比较短的架构。
* AIO：异步非阻塞，适用于连接数多且连接较长的架构。

## BIO

* Java传统的io编程。相关类和接口在java.io包下。
* BIO（blocking IO）：同步阻塞

![BIO](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/BIO.png)

### 问题分析

* 每个请求都要创建独立的线程
* 并发数较大时，创建大量线程来处理连接，系统资源占用较大
* 连接建立后，当前线程没有数据可读，线程就阻塞在Read操作上，造成线程资源浪费

## NIO

* 同步非阻塞IO
* 三大核心部分：Channel（通道），Buffer（缓冲区），Selector（选择器）

### NIO和BIO的比较

* BIO是以流的方式处理的，NIO以块的方式处理。
* BIO是阻塞的，NIO是非阻塞的。
* BIO是基于字节流和字符流进行操作的，NIO是基于Channel和Buffer进行操作的。

### NIO三大核心关系

* 每一个Channel都对应一个Buffer
* Selector对应一个线程，一个线程对应多个Channel（连接）
* 多个Channel注册到一个Selector
* 程序切换到哪个Channel是由事件决定的
* Selector会根据不同的Event，在各个Channel上切换
* Buffer就是一个内存块，底层是一个数组
* NIO数据的读取写入是通过Buffer，BIO要么是输入流或者是输出流，不能是双向，NIO的Buffer是可以读写的，需要flip方法切换
* Channel是双向的，可以返回底层操作系统的情况，比如Linux，底层的通道就是双向的

![Netty-NIO](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/Netty-NIO.png)

### 缓冲区（Buffer）

* 可以读写的一个内存块
* 容器对象（含数组）
* Buffer类是一个顶层父类，是一个抽象类
  * ByteBuffer
  * ShortBuffer
  * CharBuffer
  * ......
* Buffer类属性
  * capacity：容量，可以容纳的最大数据量，在缓冲区创建时设定，不能改变
  * limit：表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作，且极限是可以修改的
  * position：位置，下一个被读写的元素的索引
  * mark：标记

### 通道（Channel）

* 通道可以进行读写
* Channel是一个接口
* 常用类：FileChannel（文件IO）、ServerSocketChannel和SocketChannel、DatagramChannel

### 选择器（Selector）

* Selector能够检测多个注册的通道上是否有事件发生。多个通道以事件的方式注册到同一个Selector。
* Netty的IO线程NioEventLoop聚合了Selector

### NIO非阻塞网络编程原理

*  当客户端连接时，会通过ServerSocketChannel得到SocketChannel
* 将SocketChannel注册到Selector上，一个Selector上可以注册多个SocketChannel
* 注册后返回一个SelectionKey，会和该Selector关联
* Selector进行监听select方法，返回有事件发生的通道的个数
* 进一步得到各个SelectionKey（有事件发生）
* 再通过SelectionKey得到SocketChannel（ServerSocketChannel.accept()获得SocketChannel，SelectionKey.channel()）
* 得到SocketChannel，注册到Selector，关联一个Buffer，完成业务处理

### SelectionKey

* 表示Selector和网络通道的注册关系

### ServerSocketChannel

* 在服务器端监听新的客户端Socket连接

### SocketChannel

* 网络IO通道，具体负责进行读写操作
* NIO把缓冲区的数据写入通道，或者把通道里的数据读到缓冲区

### NIO与零拷贝

* 零拷贝是网络编程的关键
* 零拷贝有mmap（内存映射）和sendFile

* DMA：direct memory access直接内存拷贝
* 传统IO：4次拷贝3次切换
* mmap优化：
  * 将文件映射到内核缓冲区，用户空间可以共享内核空间的数据
  * 3次拷贝3次切换
* sendFile函数
  * 数据不经过用户态，直接从内核缓冲区进入到SocketBuffer，与用户态无关，就减少了一次上下文切换
  * 3次拷贝2次切换
  * 零拷贝是指从操作系统角度看，没有cpu拷贝
* sendFile优化
  * 避免了从内核缓存区拷贝到Socket buffer操作，其实还有一次cpu拷贝，信息量很少，消耗低，可以忽略不急
  * 2次拷贝2次切换

* 使用NIO零拷贝方式传递，transferTo方法。

## AIO

* jdk7引入，在IO编程汇总，常用有两种模式Reactor和Proactor。Java的NIO就是Reactor，当有事件触发时，服务器端得到通知，进行相应的处理
* 异步不阻塞IO，采用了Proactor模式

## Netty概述

* 异步的基于事件驱动的网络应用框架
* 可以快速开发高性能、高可靠的网络IO程序
* 简化NIO开发过程

### NIO存在的问题

* NIO类库和API繁杂
* NIO需要熟悉多线程和网络编程
* 开发工作量和难度大：客户端锻炼和重连
* epoll bug

### Netty优点

* NIO的API封装
* 使用方便
* 高性能、吞吐量更高
* 安全：SSL/TLS和StartTLS
* 社区活跃
* 版本：4.1.66.Final（2021-08-07）

### 线程模型

* 传统阻塞IO服务模型
  * 采用阻塞IO模式获取输入数据
  * 每个连接都需要独立的线程进行处理
  * 并发数很大时，会创建大量的线程，占用很大系统资源
  * 连接创建后，当前线程暂时没有数据可读，该线程会阻塞，造成线程资源浪费
* Reactor模式
  * 单Reactor单线程
  * 单Reactor多线程
  * 主从Reactor多线程（Netty基于，并且有改进）

### Reactor模式

* 基于IO复用模型：多个连接共用一个阻塞对象，只需要一个阻塞对象等待，无需阻塞等待所有连接
* 基于线程池复用线程资源：不必为每个连接创建线程，将连接完成后的任务分配给线程进行处理，一个线程可以处理多个连接的业务
* 反应器模式、分发者模式、通知者模式
* 流程：
  * 一个或多个请求传递给服务处理器
  * 服务器端程序处理多个请求，并将它们同步分派到相应的处理线程
  * Reactor模式使用IO复用监听事件，收到事件后，分发给某个线程（进程）
* 核心
  * Reactor
  * Handlers：处理程序执行IO事件要完成的实际事件

### 单Reactor单线程

* 可以监听多路连接请求
* 单个线程处理时，还是无法应对高并发
* 处理单个任务时，无法处理其他事件
* 适用场景：客户端数量有限，业务处理非常快速

### 单Reactor多线程

* Reactor通过select监控客户端请求事件，收到事件后，通过dispatch进行分发
* 连接请求，会通过Acceptor处理连接请求，然后创建一个Handler处理完成连接后的各种事件
* 如果不是连接请求，有Reactor分发调用连接对应的Handler来出来
* Handler只负责响应事件，不做具体业务，由具体Worker线程池的某个线程处理业务
* Worker线程池分配独立的线程完成真正的业务，并将结果返回给Handler
* Handler收到响应后，通过send将结果返回给Client
* 优点：
  * 可以利用多核cpu的处理能力
* 缺点
  * 多线程数据共享和访问复杂
  * 单个Reactor处理事件的响应和监听，在单线程运行，无法应对高并发

### 主从Reactor多线程

* MainReactor线程负责处理连接
* SubReactor负责处理accept外的事件
* 优点：
  * 主从线程分工明确，数据交互简单
* 缺点
  * 编程难度复杂

## Netty模型

* Netty主要基于主从Reactor多线程模型
* 简单流程
  * BossGroup的Selector只关注Accept
  * 当接收到Accept事件，获取到对应的SocketChannel，封装成NIOSocketChannel，并注册到WokerGroup中
  * 当Woker 线程监听到Selector中有自己感兴趣的事件后，就进行处理Handler
* 进阶版
  * BossGroup可以是多个NioEventLoop
  * WorkerGroup可以是多个NioEventLoop

### 详细版

* 抽象出两组线程池BoosGroup（只负责客户端的连接），WorkerGroup（专门负责网络的读写）
* BoosGroup和WorkerGroup类型都是NioEventLoopGroup
* NioEventLoopGroup相当于一个事件循环组，这个组中含有过个事件循环，每一个事件循环是NioEventLoop
* NioEventLoop表示一个不断循环的执行处理任务的线程，每个NioEventLoop都有一个Selector，用于监听绑定在其上的socket的网络通讯
* NioEventLoopGroup可以有个多个线程，即可以含有多个NioEventLoop
* 每个BoosNioEventLoop
  * 轮询accept事件
  * 处理accept事件与client建立连接，生成NioSocketChannel，并将其注册到某个Worker NioEventLoop上的Selector
  * 处理任务队列的任务
* 每个WorkerGroup下NioEventLoop
  * 轮询read、write事件
  * 处理io事件，在对应的NioSocketChannel处理
  * 处理队列任务

* 每个NioEventLoop处理任务会使用管道Pipeline，通过Pipeline可以获取到通道，其中维护了很多的处理器

### 任务队列

* 用户自定义普通任务，提交到了taskQueue
* 用户自定义定时任务，提交到了scheduleTaskQueue
* 非当前Reactor线程调用Channel的各种方法
  * 推送系统

## 异步模型

* 异步过程调用后，调用者不能立即获取到结果
* Netty的IO操作是异步的，ChannelFuture
* Future-Lister机制，通过状态判断或注册监听器来进行处理

### Future

* 表示异步执行的结果，通过它提供的方法来检测执行是否完成，比如检索计算
* ChannelFuture是一个接口，可以添加监听器，当监听的事件发生时，就会通知到监听器

## Netty核心组件

### Bootstrap、ServerBootstrap

* Bootstrap是客户端程序启动引导类
* ServerBootstrap是服务端启动引导类

### Future、ChannelFuture

* 异步操作

### Channel

* 网络通信组件

### Selector

* 实现IO多路复用

### ChannelHandler

* 处理拦截IO事件和操作

### Pipeline、ChannelPipeline

* ChannelPipeline是一个Handler集合，它负责处理和拦截inbound和outboud的事件和操作。
* ChannelPipeline实现了一种高级形式的拦截过滤器模式，用户可以完全控制事件的处理方式，以及Channel中各个ChannelHandler如何相互交互
* 一个Channel有且仅有一个ChannelPipeline与之对应
* 一个ChannelPipeline中维护了一个由ChannelHandlerContext组成的双向链表，并且每一个ChannelHandlerContext中又关联一个ChannelHandler

### ChannelHandlerContext

* 保存Channel相关的上下文信息
* 包含ChannelHandler
* 绑定了对应的Pipeline
* 真实类型：DefaultChannelHandlerContext

### ChannelOption

* 

### EventLoopGroup、NioEventLoopGroup

* 每个EventLoop维护者一个Selector实例

### Unpooled

* 专门操作缓冲区的工具类
* ByteBuf
  * 不需要filp反转
  * readerIndex：下一个读取的位置
  * writerIndex：下一个写的位置
  * capacity 
  * ByteBuf b = Unpooled.buffer(10);
  * ByteBuf b = Unpooled.copiedBuffer("hello", Charset.forName("utf-8"));

## 心跳检测

* 服务器端的childHandler，IdleStateHandler处理IdleStateEvent事件
* 当IdleStateEvent事件发生后就会将事件传递给下一个handler，在userEventTriggered中进行处理

## websocket实现长链接

* http协议是无状态的
* 浏览器和服务器相互感知
* 在childHandler中增加WebSocketServerProtocolHandler，可以将http协议升级为websocket协议，保持长链接

## Google的Protobuf

### 编码解码

* 数据在网络中传输的都是二进制字节码数据，发送数据时就需要编码，接收数据就需要解码
* codec（编解码器）：decoder（解码器）、encoder（编码器）
* Netty提供了很多编解码器：StringEncoder...StringDecoder...
  * 使用的是Java序列化技术
  * 无法跨语言
  * 序列化后体积太大，是二进制编码的5倍多
  * 序列化性能太低
* 引入新的解决方案：Google的Protobuf

### Protobuf

* 是一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或序列化
* 适合做数据存储或RPC数据交换格式
* http+json→tcp+Protobuf
* 以message的方式管理数据的
* 支持跨平台、跨语言（大多数）
* 高性能，高可靠
* 使用protobuf编译器能自动生成代码，生成.proto文件，通过proto.exe生成.java文件

### Netty中使用Protobuf步骤

* 引入相关jar包
* 编写.proto文件
* 使用proto.exe生成Java类
* 在服务端handler中发送Java类相关数据，在服务端加入编码器：ProtoBufEncoder
* 在客户端加入解码器,ProtoBufDecoder；在Handler中处理相关数据

### ProtoBuf文件编写

* Protobuf可以使用message管理其他的message

## Netty编解码器和Handler的调用机制

*  Handler在Pipeline中，从S到C的数据流动就是入站Inbound，解码器Decoder；从C到S的数据流动就是出站Outbound，编码器Encoder。

## TCP粘包和拆包

* TCP是面向连接的，面向流的，提供高可靠性服务。采用了Nagle算法，将多次间隔较小且数量小的数据，合成一个大的数据块，然后进行封包。
* TCP无消息保护边界，需要在接收端处理消息边界问题。也就是我们说的粘包拆包问题。

### 解决方案

* 自定义协议（MessageProtocol）+编解码器
* 关键是解决每次读取数据长度的问题

## Netty源码解析

### Netty启动过程

* NioServerSocketChannel.doBind
* NioEventLoop.run
* EventLoopGroup是事件循环组，包含过个EventLoop
* 默认线程cpu核数*2
* Handler插入是在tail节点之前

### Netty接收请求建立连接过程

* 在EventLoop中，processSelectedKey

## ChannelPipeline、ChannelHandlerContext、ChannelHandler

* context是对handler的封装
* ChannelHandler就是用来处理IO事件
* Socket创建的时候创建Pipeline

## 心跳机制

* IdleStateHandler
* 基于EventLoop的定时任务
* 对应三个事件读事件、写事件、读写事件

## EventLoop

* 可以接收定时任务

## handler中加入线程池和context中加入线程池

* 将耗时任务添加到异步线程池中
* EventExecutorGroup线程池

## RPC

* 远程过程调用，是一个计算机通信协议。该协议允许运行于一台计算机的程序调用另一台计算机的子程序，而程序员无需额外地为这个交互作用编程

* 两个或多个应用程序都分布在不同的服务器上，它们之间的调用都像是本地方法调用一样。

  

