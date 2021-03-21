package com.olive.java.start.java8.bulider;

import com.olive.java.start.java8.entity.Person;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/23 18:53
 */
public class BuilderTest {

    public static void main(String[] args) {

        Person person = Builder.of(Person::new)
                .with(Person::setName, "Clare")
                .with(Person::setAge, 26)
                .with(Person::setVitalStatistics, 33, 23, 33)
                .with(Person::setBirthday, "2001-10-26")
                .with(Person::setAddress, "上海")
                .with(Person::setMobile, "18888888888")
                .with(Person::setEmail, "clare@qq.com")
                .with(Person::setHairColor, "黑色直发")
                .with(Person::addHobby, "读书")
                .with(Person::addHobby, "购物")
                .with(Person::addHobby, "玩耍")
                .with(Person::addGift, "情人节礼物", "LBR 1912女王时代")
                .with(Person::addGift, "生日礼物", "迪奥烈焰蓝金")
                .with(Person::addGift, "纪念日礼物", "阿玛尼红管唇釉")
                .build();

        System.out.println(person);
    }
}
