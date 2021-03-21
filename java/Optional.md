# Optional处理null对象

* Optional 是一个容器对象，可以**存储对象**、**字符串**等值，当然也可以存储 **null 值**。
* 能帮助我们将 Java 中的对象等一些值存入其中，这样我们就不用显式进行空值检测。
* 使用 Optional 可以帮助我们解决业务中，减少值动不动就抛出空指针异常问题，也**减少 null 值的判断**，提高代码可读性等。
* of()
  * 可以将值存入 Optional 容器中，如果存入的值是 null 则抛异常。
* ofNullable()
  * 可以将值存入 Optional 容器中，即使值是 null 也不会抛异常。
* get()
  * 可以获取容器中的值，如果值为 null 则抛出异常。
* getElse()
  * 可以获取容器中的值，如果值为 null 则返回设置的默认值。
* isPresent()
  * 该方法可以判断存入的值是否为空。

![image-20210308110708901](https://cdn.jsdelivr.net/gh/ClareTung/ImageHostingService/img/image-20210308110708901.png)

