package com.olive.java.start.java8;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @description: Optional判空
 * @program: olive
 * @author: dtq
 * @create: 2021/3/8 11:09
 */
public class OptionalJudgeNull {

    @Test
    public void testOf() {
        // 传入正常值，正常返回一个 Optional 对象
        Optional<String> optional1 = Optional.of("clare");
        // 传入参数为 null，抛出 NullPointerException
        Optional<Object> optional2 = Optional.of(null);
    }


    @Test
    public void testOfNullable() {
        // 传入正常值，正常返回一个 Optional 对象
        Optional<String> optional1 = Optional.ofNullable("clare");
        // 传入 null 参数，正常返回 Optional 对象
        Optional<Object> optional2 = Optional.ofNullable(null);
    }

    @Test
    public void testIsPresent() {
        // 传入正常值，正常返回一个 Optional 对象，并使用 isPresent 方法，结果为 true
        Optional<String> optional1 = Optional.ofNullable("clare");
        System.out.println("传入正常值：" + optional1.isPresent());

        // 传入参数为 null 生成一个 Optional 对象，并使用 isPresent 方法，结果为 false
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println("传入空值：" + optional2.isPresent());
    }

    @Test
    public void testGet() {
        // 传入正常值，正常返回一个 Optional 对象，并使用 get 方法，获取到正常值
        Optional<String> optional1 = Optional.ofNullable("clare");
        System.out.println(optional1.get());

        // 传入参数为 null 生成一个 Optional 对象，并使用 get 方法，发生异常: NoSuchElementException: No value present
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.get());
    }

    @Test
    public void testIfPresent() {
        // 创建 Optional 对象，然后调用 Optional 对象的 ifPresent 方法，传入 Lambda 表达式
        Optional<String> optional1 = Optional.ofNullable("clare");
        optional1.ifPresent((value) -> System.out.println("正常值为：" + value));

        // 创建 Optional 对象，调用 Optional 对象的 ifPresent 方法，传入实现 Consumer 匿名内部类
        Optional<Object> optional2 = Optional.ofNullable("tung");
        Consumer<Object> consumer = new Consumer<Object>() {
            @Override
            public void accept(Object value) {
                System.out.println("Optional的值为" + value);
            }
        };
        optional2.ifPresent(consumer);
    }

    @Test
    public void testOrElse() {
        // 传入正常参数，获取一个 Optional 对象，并使用 orElse 方法设置默认值
        Optional<String> optional1 = Optional.ofNullable("clare");
        String actualValue = optional1.orElse("默认值");
        System.out.println("first actual value is " + actualValue);

        // 传入 null 参数，获取一个 Optional 对象，并使用 orElse 方法设置默认值
        Optional<Object> optional2 = Optional.ofNullable(null);
        Object actualValue2 = optional2.orElse("默认值");
        System.out.println("second actual value is " + actualValue2);
    }

    @Test
    public void testOrElseGet() {
        // 传入正常参数，获取一个 Optional 对象，并使用 orElseGet 方法， 返回正常值
        Optional<String> optional1 = Optional.ofNullable("clare");
        String actualValue = optional1.orElseGet(() -> {
            String defaultVal = "执行逻辑和生成的默认值";
            return defaultVal;
        });
        System.out.println("first actual value is " + actualValue);

        // 传入 null 参数，获取一个 Optional 对象，并使用 orElseGet 方法， 返回lambda表达式的值
        Optional<Object> optional2 = Optional.ofNullable(null);
        Object actualValue2 = optional2.orElseGet(() -> {
            String defaultVal = "执行逻辑和生成的默认值";
            return defaultVal;
        });
        System.out.println("second actual value is " + actualValue2);
    }

    @Test
    public void testOrElseThrow() {
        // 传入正常参数，获取一个 Optional 对象，并使用 orElseThrow 方法
        try {
            Optional optional1 = Optional.ofNullable("clare");
            Object object1 = optional1.orElseThrow(() -> {
                        System.out.println("执行逻辑，然后抛出异常");
                        return new RuntimeException("抛出异常");
                    }
            );
            System.out.println("输出的值为：" + object1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 传入 null 参数，获取一个 Optional 对象，并使用 orElseThrow 方法
        try {
            Optional optional2 = Optional.ofNullable(null);
            Object object2 = optional2.orElseThrow(() -> {
                        System.out.println("执行逻辑，然后抛出异常");
                        return new RuntimeException("抛出异常");
                    }
            );
            System.out.println("输出的值为：" + object2);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void testMap() {
        // 创建 map 对象
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name1", "clare");
        userMap.put("name2", null);

        // 传入 Map 对象参数，获取一个 Optional 对象，获取 name1 属性
        Optional<String> optional1 = Optional.of(userMap).map(value -> value.get("name1"));

        // 传入 Map 对象参数，获取一个 Optional 对象，获取 name2 属性
        Optional<String> optional2 = Optional.of(userMap).map(value -> value.get("name2"));

        // 获取 Optional 的值
        System.out.println("获取的 name1 的值：" + optional1.orElse("默认值"));
        System.out.println("获取的 name2 的值：" + optional2.orElse("默认值"));
    }

    @Test
    public void testFlatMap() {
        // 创建 map 对象
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", "clare");
        userMap.put("sex", "男");

        // 传入 Map 对象参数，获取一个 Optional 对象
        Optional<Map<String, String>> optional1 = Optional.of(userMap);

        // 使用 Optional 的 flatMap 方法，获取 Map 中的 name 属性
        // 然后通过获取的值手动创建一个新的 Optional 对象
        Optional optional2 = optional1.flatMap(value -> Optional.ofNullable(value.get("name")));

        // 获取 Optional 的 value
        System.out.println("获取的 Optional 的值：" + optional2.get());

    }

    @Test
    public void testFilter() {
        // 创建一个测试的 Optional 对象
        Optional<String> optional = Optional.ofNullable("clare");
        // 调用 Optional 的 filter 方法，设置一个满足的条件，然后观察获取的 Optional 对象值是否为空
        Optional optional1 = optional.filter((value) -> value.length() > 2);
        System.out.println("Optional 的值不为空：：" + optional1.isPresent());

        // 调用 Optional 的 filter 方法，设置一个不满足的条件，然后观察获取的 Optional 对象值是否为空
        Optional optional2 = optional.filter((value) -> value.length() < 2);
        System.out.println("Optional 的值不为空：：" + optional2.isPresent());
    }




}
