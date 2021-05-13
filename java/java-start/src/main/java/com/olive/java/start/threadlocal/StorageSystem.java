package com.olive.java.start.threadlocal;

import lombok.Builder;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/5/13 17:15
 */
@Builder
public class StorageSystem {

    /**
     * 扣库存
     */
    public void decrement() {
        // 获取用户信息
        Admin admin = AdminStorage.ADMIN.get();

        System.out.println("admin:" + admin.getName() + "扣库存");
    }
}
