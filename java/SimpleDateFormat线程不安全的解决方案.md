#  SimpleDateFormat线程不安全的解决方案

1. **将 `SimpleDateFormat` 定义为局部变量；**
2. **使用 `synchronized` 加锁执行；**
3. **使用 `Lock` 加锁执行（和解决方案 2 类似）；**
4. **使用 `ThreadLocal`；**
5. **使用 `JDK 8` 中提供的 `DateTimeFormat`。**
   1. 使用 `DateTimeFormatter` 必须要配合 `JDK 8` 中新增的时间对象 `LocalDateTime` 来使用，因此在操作之前，我们可以先将 `Date` 对象转换成  `LocalDateTime`，然后再通过 `DateTimeFormatter` 来格式化时间。

```java
private StringBuffer format(Date date, StringBuffer toAppendTo,
                                FieldDelegate delegate) {
    // 注意此行代码
    calendar.setTime(date);
```





