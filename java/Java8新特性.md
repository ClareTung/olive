## 理解函数式编程

* 函数式编程是一种思维方式，函数式编程是你要告诉代码你要做什么，命令式编程是你告诉代码要怎么做。
* 函数式编程是基于某种语法或调用API去进行编程。



## 函数式接口

* 可以使用注解`@FunctionalInterface`进行声明
* **有且只有一个抽象方法**
* 可以有默认方法，`defaullt`修饰的方法。这个特性可以在以前所编写的一些接口上提供默认实现，并且不会影响任何的实现类以及既有的代码。当接口多重继承时，可能会发生默认方法覆盖的问题，这时可以去指定使用哪一个接口的默认方法实现。
* 可以有静态方法
* 允许有Object类中继承的方法



## 方法引用

* 使用`::`来表示，是函数式接口的一种写法
* 表现形式
  * 静态方法引用，通过类名::静态方法名
  * 实例方法引用，通过实例对象::实例方法名
  * 构造方法引用，通过类名::new
  * 引用某个类型任意对象的实例方法，String::toString
* Lambda表达式也是一种函数式接口表达方式，一般Lambda表达式自己提供方法体，而方法引用一般直接引用现成的方法



## Optional可选值

* 构造函数设置为私有private，用静态of方法来代替构造函数。



## Lambda表达式

* Lambda表达式强调了输入和输出。箭头的左边是输入，右边则是输出。
* Lambda表达式的依据是必须有相应的函数接口，Lambda表达式另一个依据是类型推断机制，在上下文信息足够的情况下，编译器可以推断出参数表的类型，而不需要显式指名。
* 在JDK1.8里默认会将这个匿名类里访问的外部变量给设置为final。
* Lambda 表达式由三部分构成：参数列表、箭头（->）、Lambda 体。
*  Lambda 体，可以是一个表达式，也可以是一个语句块。、
* Lambda 体，表达式中不能加入 return 语句，因为在表达式中已经隐含了 return 语句；但是，语句块中没有隐含，需要使用 return 语句。
* 形参列表的数据类型会自动判断。
* 如果形参列表为空，只需保留（）。
* 如果形参只有一个，（）可以省略，只需要参数的名称即可。
* 如果执行语句只有一句，且无返回值，则{}可省略；如果有返回值，若想省略{}，则必须同时省略return，且执行语句也必须保证只有一句。



## Stream

* 将要处理的数据看作一种流，可以在流中对数据进行筛选、排序、聚合等。
* 流的两种操作：
  * 中间操作，每次返回一个新的流，可以有多个。
  * 终端操作，每个流只能进行一次终端操作，终端操作结束后流无法再次使用。终端操作会产生一个新的集合或值。
* stream不存储数据，而是按照特定的规则对数据进行计算，一般会输出结果。
* stream不会改变数据源，通常情况下会产生一个新的集合或一个值。
* stream具有延迟执行特性，只有调用终端操作时，中间操作才会执行。
* Stream的创建方法
  * 通过 `java.util.Collection.stream()` 方法用集合创建流
  * 使用`java.util.Arrays.stream(T[] array)`方法用数组创建流
  * 使用`Stream`的静态方法：`of()、iterate()、generate()`

**map：将一个流的元素按照一定的映射规则映射到另一个流中**

**flatMap：将多个流合并成一个流**

**foreach/find/match：遍历、匹配**

**filter：按照指定规筛选流中的数据，形成新的流**

**聚合（max/min/count)**

**collect：收集成一个值也可以收集成一个新的集合，`collect`主要依赖`java.util.stream.Collectors`类内置的静态方法。**收集器，一种通用的、从流生成复杂值的结构。收集器可以从 java.util.stream.**Collectors** 类中静态导入的。

* toList/toSet/toMap：将流中数据处理完成后归集到新的集合里
* count/averaging：统计
  * 计数：count
  * 平均值：averagingInt、averagingLong、averagingDouble
  * 最值：maxBy、minBy
  * 求和：summingInt、summingLong、summingDouble
  * 统计以上所有：summarizingInt、summarizingLong、summarizingDouble
* partitioningBy：分区，将stream按条件分为两个Map
* groupingBy： 分组，将集合分为多个Map
* joining：将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
* reducing：Collectors类提供的reducing方法，相比于stream本身的reduce方法，增加了对自定义归约的支持。
* sorted，中间操作。有两种排序sorted()：自然排序，流中元素需实现Comparable接口sorted(Comparator com)：Comparator排序器自定义排序
* distinct：去重
* limit：限制
* skip：跳过

**reduce：归约。就流中的组合起来进行操作，生成一个值，可以用来拼接，求和，求最值等等**







