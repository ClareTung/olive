[TOC]

# ThreadLocal

* ThreadLocal实例通常是类中的 private static 字段，它们希望将状态与某一个线程（例如，用户 ID 或事务 ID）相关联。

* ThreadLocal还有一个特别重要的静态内部类**ThreadLocalMap**，该类才是实现线程隔离机制的关键。get()、set()、remove()都是基于该内部类进行操作，ThreadLocalMap用键值对方式存储每个线程变量的副本，key为当前的ThreadLocal对象，value为对应线程的变量副本。
* 每个线程都有自己的ThreadLocal对象，也就是都有自己的ThreadLocalMap，对自己的ThreadLocalMap操作，当然是互不影响的了，这就不存在线程安全问题了，所以ThreadLocal是以空间来交换安全性的解决思路。


## 基础使用

`ThreadLocal` 常用的核心方法有三个：

1. **set 方法：用于设置线程独立变量副本。**没有 set 操作的 ThreadLocal 容易引起脏数据。
2. **get 方法：用于获取线程独立变量副本。**没有 get 操作的 ThreadLocal 对象没有意义。
3. **remove 方法：用于移除线程独立变量副本。**没有 remove 操作容易引起内存泄漏。

```java
public class ThreadLocalStart {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " storage in: " + threadName);
            // 在 ThreadLocal 中设置值
            threadLocal.set(threadName);
            // 执行方法，打印线程中设置的值
            print(threadName);
        };

        // 创建并启动线程 1
        new Thread(runnable, "MyThread-1").start();
        // 创建并启动线程 2
        new Thread(runnable, "MyThread-2").start();
    }

    /**
     * 打印线程中的 ThreadLocal 值
     *
     * @param threadName 线程名称
     */
    private static void print(String threadName) {
        try {
            // 得到 ThreadLocal 中的值
            String result = threadLocal.get();
            // 打印结果
            System.out.println(threadName + " out: " + result);
        } finally {
            // 移除 ThreadLocal 中的值（防止内存溢出）
            threadLocal.remove();
        }
    }
}
```



## 高级用法

### ① 初始化：initialValue

```java
private static ThreadLocal<String> threadLocal = new ThreadLocal() {
        @Override
        protected String initialValue() {
            System.out.println("执行 initialValue() 方法");
            return "默认值";
        }
    };
```

### ② 初始化2：withInitial

```java
private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "withInitial");
```

## 应用场景

> 使用 `ThreadLocal` 可以创建线程私有变量，所以不会导致线程安全问题，同时使用 `ThreadLocal` 还可以避免因为引入锁而造成线程排队执行所带来的性能消耗；再者使用 `ThreadLocal` 还可以实现一个线程内跨类、跨方法的数据传递。

### 本地变量

```java
public class MyThreadLocalByDateFormat {
    // 创建ThreadLocal并设置默认值
    private static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static void main(String[] args) {
        // 创建线程池执行任务
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 60,
                TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));

        // 执行任务
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            // 执行任务
            threadPool.execute(() -> {
                // 得到时间对象
                Date date = new Date(finalI * 1000);
                // 执行时间格式化
                formatAndPrint(date);
            });
        }

        // 线程池执行完任务之后关闭
        threadPool.shutdown();
    }

    /**
     * 格式化并打印时间
     *
     * @param date 时间对象
     */
    private static void formatAndPrint(Date date) {
        // 执行格式化
        String result = dateFormatThreadLocal.get().format(date);
        // 打印最终结果
        System.out.println("Time format result：" + result);
    }

}
```

### 跨类跨方法数据传递

```java
public class AdminStorage {

    // 用户信息
    public static ThreadLocal<Admin> ADMIN = new ThreadLocal<>();

    // 存储用户信息
    public static void setAdmin(Admin admin){
        ADMIN.set(admin);
    }

}
```

```java
public class ThreadLocalUserHolder {
    public static void main(String[] args) {
        Admin clareTung = Admin.builder().name("ClareTung").build();

        //存用户
        AdminStorage.setAdmin(clareTung);

        // 下订单
        OrderSystem.builder().build().add();

        // 扣库存
        StorageSystem.builder().build().decrement();
    }
}
```

```java
public class OrderSystem {

    /**
     * 下订单
     */
    public void add() {
        // 获取用户信息
        Admin admin = AdminStorage.ADMIN.get();

        System.out.println("admin:" + admin.getName() + "下订单");
    }
}
```

```java
public class StorageSystem {

    /**
     * 扣库存
     */
    public void decrement() {
        // 获取用户信息
        Admin admin = AdminStorage.ADMIN.get();

        System.out.println("admin:" + admin.getName() + "扣库存");
    }
}

```
## ThreadLocal为什么会内存泄漏

* ThreadLocalMap的key为ThreadLocal实例，他是一个弱引用，我们知道弱引用有利于GC的回收，当key == null时，GC就会回收这部分空间，但value不一定能被回收，因为他和Current Thread之间还存在一个强引用的关系。
  由于这个强引用的关系，会导致value无法回收，如果线程对象不消除这个强引用的关系，就可能会出现OOM。有些时候，我们调用ThreadLocalMap的remove()方法进行显式处理。



