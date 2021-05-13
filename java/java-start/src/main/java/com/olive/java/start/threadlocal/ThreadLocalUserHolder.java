package com.olive.java.start.threadlocal;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/5/13 17:24
 */
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
