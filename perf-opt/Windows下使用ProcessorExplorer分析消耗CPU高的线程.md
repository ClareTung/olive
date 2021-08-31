

## Windows下使用ProcessorExplorer分析线程堆栈

* 生成线程堆栈文件

```
PS C:\Users\EDZ> jps -l
11120 org.jetbrains.jps.cmdline.Launcher
43988
28728 org.jetbrains.kotlin.daemon.KotlinCompileDaemon
35784 org.jetbrains.idea.maven.server.RemoteMavenServer36
24428 com.olive.perf.opt.PerfOptApplication
7756 ApacheJMeter.jar
8316 sun.tools.jps.Jps
PS C:\Users\EDZ> jstack -l 24428 > d:\21083104.stack
PS C:\Users\EDZ>
```



* 查看线程

![image-20210831183753866](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210831183753866.png)

* 转义线程id为十六进制，查找stack文件



![image-20210831183829821](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210831183829821.png)

* 搜索堆栈文件

![image-20210831183937937](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210831183937937.png)

* 查看代码

![image-20210831184157925](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210831184157925.png)