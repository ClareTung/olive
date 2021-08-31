package com.olive.perf.opt;

import java.util.LinkedList;
import java.util.List;

/**
 * @description: 内存溢出
 * @program: olive
 * @author: dtq
 * @create: 2021/8/31 14:00
 */
public class OutOfMemory {

    // -Xms5m -Xmx5m  -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=E:\oomDump\

    public static void main(String[] args) {
        List<Object> list = new LinkedList<>();
        int i = 0;
        while (true) {
            i++;
            if (i % 10000 == 0) {
                System.out.println("i=" + i);
            }
            list.add(new Object());
        }
    }
}
