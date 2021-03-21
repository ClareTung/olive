package com.olive.java.start.java8;

import com.olive.java.start.java8.entity.User;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 函数式编程
 */
public class FunctionalProgramming {


    public static void main(String[] args) {
        // 方法引用
        Function<String, Integer> func1 = Integer::parseInt;
        Integer result1 = func1.apply("5");

        User user = new User(1L, "Clare", 26);
        Function<User, String> func2 = user::getUserInfo;
        System.out.println(func2.apply(user));

        // Optional可选值
        String msg = "hello";
        Optional<String> optional = Optional.of(msg);
        // 判断是否有值
        boolean present = optional.isPresent();
        // 有值就返回，否则抛出异常
        String value = optional.get();
        // 如果为空，就返回指定值
        value = optional.orElse("hi");
        // 返回对象时，判断为空，可以new一个对象返回

        // 函数式编程
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

        int min = IntStream.of(nums).min().getAsInt();
        System.out.println(min);

        // 函数式接口
        // 输入T输出R
        Function<Integer, String> moneyFormat = i -> new DecimalFormat("#,###").format(i);
        // 断言函数接口
        Predicate<Integer> predicate = i -> i > 0;
        // 消费函数接口
        Consumer<String> consumer = System.out::println;
        // 提供数据接口
        Supplier<Integer> supplier = () -> 10 + 1;
        // 一元函数接口
        UnaryOperator<Integer> unaryOperator = i -> i * 2;
        // 二元函数接口
        BinaryOperator<Integer> binaryOperator = (a, b) -> a * b;

        // Lambda表达式对集合的遍历
        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");
        map.put("d", "d");

        map.forEach((k, v) -> System.out.println("k=" + k + "，v=" + v));

        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("bb");
        list.add("ccc");
        list.add("dddd");

        list.forEach(System.out::println);

        // Lambda代替匿名类
        Runnable runnable = () -> System.out.println("lambda");

        // Stream的创建
        // 从集合中创建
        List<String> strList = Arrays.asList("a", "b", "c");
        // 创建一个顺序流
        Stream<String> stream = strList.stream();
        // 创建一个并行流
        Stream<String> parallelStream = strList.parallelStream();
        // 使用java.util.Arrays.stream(T[] array)方法用数组创建流
        int[] array = {1, 3, 5, 6, 8};
        IntStream arrStream = Arrays.stream(array);
        // 使用Stream的静态方法：of()、iterate()、generate()
        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4, 5, 6);
        Stream<Integer> stream2 = Stream.iterate(0, (x) -> x + 3).limit(4);
        Stream<Double> stream3 = Stream.generate(Math::random).limit(3);

        // map
        Stream.of("apple", "pear", "watermelon", "orange")
                .map(String::length)
                .forEach(System.out::println);

        // flatMap
        Stream.of("a-b-c-1-d", "2-r-ty-s")
                .flatMap(e -> Stream.of(e.split("-")))
                .forEach(System.out::println);

        // foreach/find/match
        Stream.of(2, 5, 3, 7, 6)
                .forEach(System.out::println);
        Optional<Integer> first = Stream.of(2, 5, 3, 7, 6)
                .filter(x -> x > 3)
                .findFirst();
        boolean b = Stream.of(2, 5, 3, 7, 6)
                .allMatch(x -> x > 3);

        // filter
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(e -> e > 4)
                .forEach(System.out::println);

        // 聚合（max/min/count)
        long count = Stream.of("apple", "pear", "watermelon", "orange")
                .count();
        Optional<String> max = Stream.of("apple", "pear", "watermelon", "orange")
                .max(Comparator.comparing(String::length));
        Optional<Integer> min2 = Stream.of(2, 5, 3, 7, 6)
                .min(Integer::compareTo);

        // reduce
        String strReduce = Stream.of("apple", "pear", "watermelon", "orange")
                .filter(s -> s.length() < 6)
                .reduce(",", String::concat);
        int sumReduce = Stream.of(1, 2, 3, 4, 5, 6).reduce(Integer::sum).get();

        // collect
        // toList/toSet/toMap
        Set<String> set = Stream.of("apple", "pear", "watermelon", "orange")
                .collect(Collectors.toSet());
        Double collect = Stream.of(1, 2, 3, 4, 5, 6)
                .collect(Collectors.averagingDouble(Integer::intValue));

        // partitioningBy：分区，将stream按条件分为两个Map
        Map<Boolean, List<Integer>> collect1 = Stream.of(1, 2, 3, 4, 5, 6)
                .collect(Collectors.partitioningBy(i -> i > 4));
        System.out.println(collect1.get(true));
        System.out.println(collect1.get(false));

        /*
         * [5, 6]
         * [1, 2, 3, 4]
         */

        // groupingBy： 分组，将集合分为多个Map
        Map<Boolean, List<Integer>> collect2 = Stream.of(1, 2, 3, 4, 5, 6)
                .collect(Collectors.groupingBy(i -> i > 4));
        System.out.println(collect2.get(true));
        System.out.println(collect2.get(false));

        /*
         * [5, 6]
         * [1, 2, 3, 4]
         */

        // joining：将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
        // String collect3 = String.join("-", list);
        String collect3 = list.stream()
                .collect(Collectors.joining("-"));


        // sorted
        Stream.of(2, 6, 3, 7, 4, 5, 9, 3, 2, 1, 7)
                .sorted()
                .forEach(System.out::println);

        // limit
        Stream.of(1, 2, 3, 4, 5, 6)
                .limit(4)
                .forEach(System.out::println);

        // distinct
        Stream.of(1, 2, 6, 3, 2, 3, 4, 5, 5, 1, 1, 6)
                .distinct()
                .forEach(System.out::println);


    }
}
