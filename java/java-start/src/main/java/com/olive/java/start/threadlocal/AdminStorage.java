package com.olive.java.start.threadlocal;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/5/13 17:16
 */
public class AdminStorage {

    // 用户信息
    public static ThreadLocal<Admin> ADMIN = new ThreadLocal<>();

    // 存储用户信息
    public static void setAdmin(Admin admin){
        ADMIN.set(admin);
    }

}
