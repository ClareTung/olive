package com.olive.spirngframwork.bean;

import java.util.Random;

/**
 * 类UserService13的实现描述：UserService13
 *
 * @author dongtangqiang 2022/7/9 18:10
 */
public class UserService13 implements IUserService{

    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "clare,1,上海";
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

}