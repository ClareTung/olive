## jps

* Java Virtual Machine Process Status Tool
* 输出JVM中运行的进程状态信息
* jps -l 输出main类或jar的全限定名

## jstack

* 查看某个Java进程内的线程堆栈信息
* top -Hp pid
* printf "%x\n" pid
* jstack 21711 | grep 54ee

## Jmap和jhat

* 查看堆内存情况

## Java Thread State

### waiting for monitor entry

* 在等待进入一个临界区
* Blocked

### waiting on condition

* 在等待另一个条件的发生，来把自己唤醒
* Waiting
* Timed_waiting

### 大量线程在 waiting for monitor entry

* 可能是一个全局锁阻塞了大量线程

### 大量线程在 waiting on condition

* 可能是网络阻塞



**线程状态为“in Object.wait()”：**

说明它**获得了监视器之后，又调用了 java.lang.Object.wait() 方法**。

每个 Monitor在某个时刻，只能被一个线程拥有，该线程就是 “Active Thread”，而其它线程都是 “Waiting Thread”，分别在两个队列 “ Entry Set”和 “Wait Set”里面等候。在 “Entry Set”中等待的线程状态是 “Waiting for monitor entry”，而在 “Wait Set”中等待的线程状态是 “in Object.wait()”。

当线程获得了 Monitor，如果发现线程继续运行的条件没有满足，它则调用对象（一般就是被 synchronized 的对象）的 wait() 方法，放弃了 Monitor，进入 “Wait Set”队列。

此时线程状态大致为以下几种：

- java.lang.Thread.State: **TIMED_WAITING** (on object monitor)；
- java.lang.Thread.State: **WAITING** (on object monitor)；

一般都是RMI相关线程（RMI RenewClean、 GC Daemon、RMI Reaper），GC线程（Finalizer），引用对象垃圾回收线程（Reference Handler）等系统线程处于这种状态。

## vmstat 1

* 通过on object monitor可以找到锁竞争激烈的代码，从而找出上下文切换的原因






















