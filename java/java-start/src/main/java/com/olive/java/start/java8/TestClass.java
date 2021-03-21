package com.olive.java.start.java8;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.olive.java.start.java8.bulider.Builder;
import com.olive.java.start.java8.entity.User;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 测试类
 * @program: olive
 * @author: dtq
 * @create: 2021/3/3 15:07
 */
public class TestClass {

    /**
     * Collectors.toMap存在重复key会报错：java.lang.IllegalStateException: Duplicate key
     */
    @Test
    public void testCollectToMap() {

        User clare = Builder.of(User::new)
                .with(User::setId, 1L)
                .with(User::setAge, 25)
                .with(User::setName, "clare")
                .build();
        User tung = Builder.of(User::new)
                .with(User::setId, 2L)
                .with(User::setAge, 26)
                .with(User::setName, "qiang")
                .build();
        User qiang = Builder.of(User::new)
                .with(User::setId, 3L)
                .with(User::setAge, 24)
                .with(User::setName, "qiang")
                .build();

        List<User> userList = Lists.newArrayList(clare, tung, qiang);

        Map<String, User> nameUserMap =
                userList.stream().collect(Collectors.toMap(User::getName, Function.identity(), (u1, u2) -> u1));
        nameUserMap.forEach((name, user) -> {
            System.out.println(name + "::" + user);
        });

        Map<String, Map<Long, User>> nameIdAndUserMap = userList.stream().collect(Collectors.toMap(User::getName, user -> {
            Map<Long, User> map = Maps.newHashMap();
            map.put(user.getId(), user);
            return map;
        }, (oldValue, newValue) -> oldValue));
        nameIdAndUserMap.forEach((name, idAndUser) -> {
            System.out.println(name + "--" + idAndUser);
        });
    }

}
