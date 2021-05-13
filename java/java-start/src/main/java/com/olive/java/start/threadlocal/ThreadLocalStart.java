package com.olive.java.start.threadlocal;

/**
 * @description: ThreadLocal
 * @program: olive
 * @author: dtq
 * @create: 2021/5/13 15:42
 */
public class ThreadLocalStart {

    //    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
//    private static ThreadLocal<String> threadLocal = new ThreadLocal() {
//        @Override
//        protected String initialValue() {
//            System.out.println("执行 initialValue() 方法");
//            return "默认值";
//        }
//    };

    private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "withInitial");

    public static void main(String[] args) {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
//            System.out.println(threadName + " storage in: " + threadName);
//            // 在 ThreadLocal 中设置值
//            threadLocal.set(threadName);
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
