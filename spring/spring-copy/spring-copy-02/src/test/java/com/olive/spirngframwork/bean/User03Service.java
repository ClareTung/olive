package com.olive.spirngframwork.bean;

/**
 * 类User03Service的实现描述：测试类
 *
 * @author dongtangqiang 2022/5/27 22:24
 */
public class User03Service {
    private String name;

    public User03Service(String name) {
        this.name = name;
    }

    public void queryUserInfo() {
        System.out.println("查询用户信息：" + name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("").append(name);
        return sb.toString();
    }
}
