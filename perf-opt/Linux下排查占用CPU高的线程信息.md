## Linux下排查占用CPU高的线程信息

* 找到cpu占用率高的Java进程号

```
top -c # 显示运行中的进程列表信息, shift+p使列表按spu使用率排序显示
```

* 根据进程号找到cpu占有率最高的线程号
  * 例如：pid=2228的线程消耗cpu最高，转成十六进制为`8b4`

```
top -Hp {pid}
```

* 利用jstack生成虚拟机中所有线程的快照

```
jps -l
jstack -l 2227 > /opt/2227.stack
```

* 下载2222.stack到本地，进行分析。在Linux上分析

```
cat 2227.stack | grep '8b4' -C 5
```

