## Linux下分析内存泄露

* 找到内存占有率最高的进程号

```
top -c        # shift+m按内存使用率进行排序
```

* 利用`jmap`生成堆转储快照

```
jmap -dump:format=b,file=/opt/heapdump_2527.hprof 2527
```

* 利用MAT分析堆转储快照

![image-20210831203043141](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210831203043141.png)

![image-20210831203259689](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210831203259689.png)