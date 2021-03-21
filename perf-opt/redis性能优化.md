## redis lettuce

* lettuce客户端连接方式是基于netty的多路复用异步非阻塞的连接方案。
* 受制于CPU核数因此增大连接数反而增加了线程上下文切换时间。
* 线程池大小：  ①CPU核数+1；② 2倍CPU核数