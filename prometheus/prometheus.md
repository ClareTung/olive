# prometheus

[TOC]

## 概述

* Prometheus 是一款基于时序数据库的开源**监控告警系统**。

* Prometheus 作为一个微服务架构监控系统的解决方案，它和容器也脱不开关系。

* 云原生由微服务架构、DevOps 和以容器为代表的敏捷基础架构组成，帮助企业快速、持续、可靠、规模化地交付软件。

* 监控系统必须满足下面四个特性：

  * 多维度数据模型
  * 方便的部署和维护
  * 灵活的数据采集
  * 强大的查询语言

* 多维度数据模型和强大的查询语言这两个特性，正是时序数据库所要求的，所以 Prometheus 不仅仅是一个监控系统，同时也是一个**时序数据库**。

* Prometheus 采用去中心化架构，可以独立部署，不依赖于外部的分布式存储，你可以在几分钟的时间里就可以搭建出一套监控系统。

* Prometheus 数据采集方式也非常灵活。要采集目标的监控数据，首先需要在目标处安装数据采集组件，这被称之为 **Exporter**，它会在目标处收集监控数据，并暴露出一个 HTTP 接口供 Prometheus 查询，Prometheus 通过 Pull 的方式来采集数据，这和传统的 Push 模式不同。不过 Prometheus 也提供了一种方式来支持 Push 模式，你可以将你的数据推送到 Push Gateway，Prometheus 通过 Pull 的方式从 Push Gateway 获取数据。目前的 Exporter 已经可以采集绝大多数的第三方数据，比如 Docker、HAProxy、StatsD、JMX 等等，官网有一份 Exporter 的列表。

  

  ![image-20210205162727850](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210205162727850.png)

* Prometheus server
  * 它负责收集和存储指标数据，支持表达式查询，和告警的生成。接下来我们就来安装 Prometheus server。
* 











