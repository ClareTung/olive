package com.olive.java.start.threadlocal;

import lombok.Builder;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/5/13 17:14
 */
@Builder
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
