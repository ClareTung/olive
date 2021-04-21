# Java 序列化和反序列化为什么要实现 Serializable 接口

[TOC]

## 序列化和反序列化

* 序列化：把对象转换为字节序列的过程称为对象的序列化
* 反序列化：把字节序列恢复为对象的过程称为对象的反序列化

## 什么时候需要用到序列化和反序列化

> **只要我们对内存中的对象进行持久化或网络传输, 这个时候都需要序列化和反序列化**

* 将内存中的对象持久化到磁盘（数据库）中时
  * 并不是将整个对象持久化到数据库中, 而是将对象中的属性持久化到数据库中, 而这些属性都是实现了 Serializable 接口的基本属性
* 与浏览器进行交互时
  * JSON格式数据已经将一个对象转化为字符串，String中已经实现Serializable接口

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;
```

* 实现RPC

## 实现序列化和反序列化为什么要实现 Serializable 接口?

* 在 Java 中实现了 Serializable 接口后, JVM 会在底层帮我们实现序列化和反序列化

## 实现 Serializable 接口就算了, 为什么还要显示指定 serialVersionUID 的值?

* 如果不显式指定 serialVersionUID, JVM 在序列化时会根据属性自动生成一个 serialVersionUID, 然后与属性一起序列化, 再进行持久化或网络传输.。在反序列化时, JVM 会再根据属性自动生成一个新版 serialVersionUID, 然后将这个新版 serialVersionUID 与序列化时生成的旧版serialVersionUID 进行比较, 如果相同则反序列化成功, 否则报错。
* 如果显式指定了 serialVersionUID, JVM 在序列化和反序列化时仍然都会生成一个serialVersionUID, 但值为我们显式指定的值, 这样在反序列化时新旧版本的 serialVersionUID 就一致了。
* 在实际开发中, 不显示指定 serialVersionUID 的情况会导致什么问题? 如果我们的类写完后不再修改, 那当然不会有问题, 但这在实际开发中是不可能的, 我们的类会不断迭代, 一旦类被修改了, 那旧对象反序列化就会报错. 所以在实际开发中, 我们都会显示指定一个 serialVersionUID, 值是多少无所谓, 只要不变就行。

### 新建一个Book类，实现Serializable接口，不指定serialVersionUID

```java
package com.olive.java.start.serializable;

import java.io.Serializable;
import java.math.BigDecimal;

public class Book implements Serializable {

    private String name;

    private BigDecimal price;

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Book setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

```

### 进行序列化

```java
package com.olive.java.start.serializable;

import java.io.*;
import java.math.BigDecimal;

public class SerializableTest {
    private static void serialize(Book book) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\\\test.txt")));
        oos.writeObject(book);
        oos.close();
    }

    private static Book deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\\\test.txt")));
        return (Book) ois.readObject();
    }


    public static void main(String[] args) throws Exception {
        Book book = new Book();
        book.setName("西游记");
        book.setPrice(BigDecimal.valueOf(45.8));
        System.out.println("序列化前的结果: " + book);

        serialize(book);
    }
}

```

### Book类中增加属性desc

```java
package com.olive.java.start.serializable;

import java.io.Serializable;
import java.math.BigDecimal;

public class Book implements Serializable {

    private String name;

    private BigDecimal price;

    private String desc;

    public String getDesc() {
        return desc;
    }

    public Book setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Book setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

```

### 进行反序列化

```java
package com.olive.java.start.serializable;

import java.io.*;
import java.math.BigDecimal;

public class SerializableTest {
    private static void serialize(Book book) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\\\test.txt")));
        oos.writeObject(book);
        oos.close();
    }

    private static Book deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\\\test.txt")));
        return (Book) ois.readObject();
    }


    public static void main(String[] args) throws Exception {
        Book book = new Book();
        book.setName("西游记");
        book.setPrice(BigDecimal.valueOf(45.8));
        System.out.println("序列化前的结果: " + book);

//        serialize(book);

        Book dBook = deserialize();
        System.out.println("反序列化后的结果: " + dBook);
    }
}

```

### 结果

```java
序列化前的结果: Book{name='西游记', price=45.8}
Exception in thread "main" java.io.InvalidClassException: com.olive.java.start.serializable.Book; local class incompatible: stream classdesc serialVersionUID = 1275391898247142065, local class serialVersionUID = 4527086674823832169
	at java.io.ObjectStreamClass.initNonProxy(ObjectStreamClass.java:699)
	at java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:1885)
	at java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1751)
	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2042)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1573)
	at java.io.ObjectInputStream.readObject(ObjectInputStream.java:431)
	at com.olive.java.start.serializable.SerializableTest.deserialize(SerializableTest.java:15)
	at com.olive.java.start.serializable.SerializableTest.main(SerializableTest.java:27)
```

### 指定serialVersionUID

```java
private static final long serialVersionUID = 1L;
```

### 重复上面的步骤测试结果

```java
序列化前的结果: Book{name='西游记', price=45.8}
反序列化后的结果: Book{name='西游记', price=45.8}
```

* 显式指定 serialVersionUID 后就解决了序列化与反序列化产生的 serialVersionUID 不一致的问题。

## Java 序列化的其他特性

* **被 transient 关键字修饰的属性不会被序列化, static 属性也不会被序列化**

```java
    private transient String desc;

    private static String content = "孙悟空大闹天空";
```

### 序列化

```java
序列化前的结果: Book{name='西游记', price=45.8, desc='五百年前', content='孙悟空大闹天空'}
```

### 修改static中的值

```java
private static String content = "西天取经";
```

### 反序列化

```java
反序列化后的结果: Book{name='西游记', price=45.8, desc='null', content='西天取经'}
```

## static 属性为什么不会被序列化?

* 因为序列化是针对对象而言的, 而 static 属性优先于对象存在, 随着类的加载而加载, 所以不会被序列化
* 看到这个结论, 是不是有人会问, serialVersionUID 也被 static 修饰, 为什么 serialVersionUID 会被序列化? 其实 serialVersionUID 属性并没有被序列化, JVM 在序列化对象时会自动生成一个 serialVersionUID, 然后将我们显示指定的 serialVersionUID 属性值赋给自动生成的 serialVersionUID

## transient关键字

* transient只能用来修饰成员变量（field），被transient修饰的成员变量不参与序列化过程。

### transient关键字设计思路和底层实现思路

* 理解transient的关键在于理解序列化，序列化是Java对象转换为字节序列。
* 详细的说，就是Java对象在电脑中是存于内存之中的，内存之中的存储方式毫无疑问和磁盘中的存储方式不同（一个显而易见的区别就是对象在内存中的存储分为堆和栈两部分，两部分之间还有指针；但是存到磁盘中肯定不可能带指针，一定是某种文本形式）。序列化和反序列化就是在这两种不同的数据结构之间做转化。
* 实现原理也是显而易见的，只要在处理两个数据结构转化的过程中，把标为transient的成员变量特殊处理一下就好了。

## 使用Externalizable自定义序列化

* 使用Externalizable接口，实现writeExternal以及readExternal这两个方法，来自己实现序列化和反序列化。
* **实现的过程中，需要自己指定需要序列化的成员变量，此时，static和transient关键词都是不生效的，因为你重写了序列化中的方法。**

```java
package com.olive.java.start.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BookExternalizable implements Externalizable {

    private String name;

    private transient String desc;

    private static String content = "西天取经";
    // private static String content = "三打白骨精";

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(content);
        out.writeUTF(desc);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        content = in.readUTF();
        desc = in.readUTF();
    }

    public String getName() {
        return name;
    }

    public BookExternalizable setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public BookExternalizable setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        BookExternalizable.content = content;
    }

    @Override
    public String toString() {
        return "BookExternalizable{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

package com.olive.java.start.serializable;

import java.io.*;

public class ExternalizableTest {

    private static void serialize(BookExternalizable book) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\\\test.txt")));
        oos.writeObject(book);
        oos.close();
    }

    private static BookExternalizable deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\\\test.txt")));
        return (BookExternalizable) ois.readObject();
    }


    public static void main(String[] args) throws Exception {
//        BookExternalizable book = new BookExternalizable();
//        book.setName("西游记");
//        book.setDesc("五百年前");
//        System.out.println("序列化前的结果: " + book);
//
//        serialize(book);

        BookExternalizable dBook = deserialize();
        System.out.println("反序列化后的结果: " + dBook);
    }
}


```

### 序列化结果

```java
序列化前的结果: BookExternalizable{name='西游记', desc='五百年前', content='西天取经'}
```

### 修改static值

```java
    private static String content = "三打白骨精";
```

### 反序列化结果

```java
反序列化后的结果: BookExternalizable{name='null', desc='五百年前', content='西天取经'}
```

* Externalizable接口中，指定的成员变量被序列化了，不管是否有static和transient关键词，但是不被指定的成员变量不能被序列化。













