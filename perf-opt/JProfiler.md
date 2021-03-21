# JProfiler

## 数据采集模式

* JProfier 提供两种数据采集模式 Sampling 和 Instrumentation。

* Sampling - 适合于不要求数据完全精确的场景。优点是对系统性能的影响较小，缺点是某些特性不支持（如方法级别的统计信息）。

* Instrumentation - 完整功能模式，统计信息也是精确的。缺点是如果需要分析的类比较多，对应用性能影响较大。为了降低影响，往往需要和 Filter 一起使用。

## CPU views

* CPU views 下的各个子视图展示了应用中各方法的执行次数、执行时间、调用关系等信息，能帮我们定位对应用性能影响最大的方法。

### Call Tree
* Call tree 通过树形图清晰地展现了方法间的层次调用关系。同时，JProfiler 将子方法按照它们的执行总时间由大到小排序，这能让您快速定位关键方法。

### Call Graph
* 找到了关键方法后，call graph 视图能为您呈现与该方法直接关联的所有方法。这有助于我们对症下药，制定合适的性能优化策略。

## Live memory
* Live memory 下的各个子视图能让您掌握内存的具体分配和使用情况，助您判断是否存在内存泄漏问题。

### All Objects
* All Objects 视图展示了当前堆中各种对象的数量和总大小。

### Allocation Call Tree
* Allocation Call Tree 以树形图的形式展示了各方法分配的内存大小。

## Thread History
* 线程历史记录视图直观地展示了各线程在不同时间点的状态。

