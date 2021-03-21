package com.olive.java.start.java8;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @description: 集合操作
 * @program: olive
 * @author: dtq
 * @create: 2021/2/5 11:32
 */
public class CollectionOperation {

    private static final Map<Integer, String> map = Maps.newHashMap();

    static {
        map.put(1, "hello");
        map.put(2, "summer");
        map.put(3, "day");
        map.put(4, "clare");
    }


    public static void main(String[] args) {
        // 集合遍历
        iterableForEach();
        // 对集合中剩余的元素进行操作
        iterableForEachRemaining();
        // 按照一定规则过滤集合中的元素
        collectionRemoveIf();
        // Stream操作
        testStream();
        // 一元运算
        listReplaceAll();
        // List排序
        listSort();
        // Map.foreach
        mapForeach();
        // Map.remove
        mapRemove();
        // Map.compute
        mapCompute();
        // 原Map原来不包括该key，那么该方法可能会添加一组key-value对
        mapComputeIfAbsent();
        // 对Map中存在的key，重新计算
        mapComputeIfPresent();
        // key存在或 value存在，则返回对应的value，否则返回defaultValue
        mapGetOrDefault();
        // 当map中不存在指定的key时，便将传入的value设置为key的值，当key存在值时，执行一个方法该方法接收key的旧值和传入的value，执行自定义的方法返回最终结果设置为key的值。
        mapMerge();
        // 不存在就放进去
        mapPutIfAbsent();
        // 指定key对应的value替换成新value
        mapReplace();
        // 对所有的key和value 进行计算，填入value中
        mapReplaceAll();
    }

    private static void mapReplaceAll() {
        map.replaceAll((k, v) -> k + "-" + v);

        mapForeach();
        System.out.println("----------------Map.replaceAll-end-----------------");
    }

    private static void mapReplace() {
        map.replace(1, "你好", "hi");

        mapForeach();
        System.out.println("----------------Map.replace-end-----------------");
    }

    private static void mapPutIfAbsent() {
        map.putIfAbsent(5, "dd");

        mapForeach();
        System.out.println("----------------Map.putIfAbsent-end-----------------");
    }

    private static void mapMerge() {
        map.merge(4, "Tung", (oldValue, newValue) -> oldValue + newValue);

        mapForeach();
        System.out.println("----------------Map.merge-end-----------------");
    }

    private static void mapGetOrDefault() {
        String result = map.getOrDefault(5, "Tung");

        System.out.println(result);
        System.out.println("----------------Map.getOrDefault-end-----------------");
    }

    private static void mapComputeIfPresent() {
        map.computeIfPresent(1, (k, v) -> v = "你好");

        mapForeach();
        System.out.println("----------------Map.computeIfPresent-end-----------------");
    }

    private static void mapComputeIfAbsent() {
        map.computeIfAbsent(3, k -> "暖洋洋");

        mapForeach();
        System.out.println("----------------Map.computeIfAbsent-end-----------------");
    }

    private static void mapCompute() {
        String compute = map.compute(2, (k, v) -> v = "夏天");
        System.out.println(compute);

        mapForeach();

        System.out.println("----------------Map.compute-end-----------------");
    }

    private static void mapRemove() {
        // key存在且key对应的value确实是传入的value才移除
        map.remove(3, "day");

        mapForeach();

        System.out.println("----------------Map.remove-end-----------------");
    }

    private static void mapForeach() {
        map.forEach((x, y) -> {
            System.out.println(x + "::" + y);
        });

        System.out.println("----------------Map.foreach-end-----------------");
    }

    private static void listSort() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        list.sort((x, y) -> y - x);
        list.forEach(System.out::println);

        System.out.println("----------------List.sort-end-----------------");
    }

    private static void listReplaceAll() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        list.replaceAll(x -> x + 10);
        list.forEach(System.out::println);

        System.out.println("----------------List.replaceAll-end-----------------");
    }

    private static void testStream() {
        IntStream stream = IntStream.builder()
                .add(1)
                .add(2)
                .add(3)
                .build();
        int max = stream.max().getAsInt();
        System.out.println(max);

        System.out.println("----------------stream-end-----------------");
    }

    private static void collectionRemoveIf() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        list.removeIf(x -> x % 2 == 0);
        list.forEach(System.out::println);

        System.out.println("----------------Collection.removeIf-end-----------------");
    }

    private static void iterableForEach() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        // 集合遍历
        list.forEach(System.out::println);

        System.out.println("----------------Iterable.foreach-end-----------------");
    }

    private static void iterableForEachRemaining() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Iterator<Integer> iterator = list.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            i++;
            if (i == 5) {
                break;
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>");

        // 调用forEachRemaining()方法遍历集合元素
        iterator.forEachRemaining(System.out::println);

        System.out.println("----------------Iterable.forEachRemaining-end-----------------");
    }
}
