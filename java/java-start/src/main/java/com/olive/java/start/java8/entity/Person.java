package com.olive.java.start.java8.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/23 18:53
 */
@Data
public class Person {
    private String name;
    private int age;
    private int bust;
    private int waist;
    private int hips;
    private List<String> hobby;
    private String birthday;
    private String address;
    private String mobile;
    private String email;
    private String hairColor;
    private Map<String, String> gift;

    // 聚合方法
    public void addHobby(String hobby) {
        this.hobby = Optional.ofNullable(this.hobby).orElse(Lists.newArrayList());
        this.hobby.add(hobby);
    }

    public void addGift(String day, String gift) {
        this.gift = Optional.ofNullable(this.gift).orElse(Maps.newHashMap());
        this.gift.put(day, gift);
    }

    public void setVitalStatistics(int bust, int waist, int hips) {
        this.bust = bust;
        this.waist = waist;
        this.hips = hips;
    }
}
