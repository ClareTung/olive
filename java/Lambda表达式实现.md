## Java Lambda表达式实现

```java
public class LambdaTest {
    public static void main(String[] args) {
        new Thread(() -> System.out.println("Lambda实现"));

        List<String> intList = IntStream.range(0, 5).boxed().filter(i -> i < 3).map(i -> i + "").collect(Collectors.toList());
        System.out.println(intList);
    }
}
```

* 编译

```
E:\ijWorkPlace\clare\java\src\test\java>javac LambdaTest.java
```

* 查看字节码文件

```
E:\ijWorkPlace\clare\java\src\test\java>javap -p LambdaTest.class
Compiled from "LambdaTest.java"
public class LambdaTest {
  public LambdaTest();
  public static void main(java.lang.String[]);
  private static java.lang.String lambda$main$2(java.lang.Integer);
  private static boolean lambda$main$1(java.lang.Integer);
  private static void lambda$main$0();
}
```

* **Lambda 表达式在 Java 中最终编译成`私有的静态函数`，JDK 最终使用 invokedynamic 字节码指令调用。**

### 为什么不使用匿名内部类实现

* 每个匿名内部类都会在啊编译时创建一个对应的class文件，在运行时不可避免的会有加载、验证、准备、解析、初始化等类加载过程。
* 每次调用都会创建一个这个匿名内部类class的实例对象，无论是有状态的（使用了外部的变量）还是无状态（没有使用外部变量）的内部类。

### invokedynamic

![invokedynamic](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/invokedynamic.jpg)

