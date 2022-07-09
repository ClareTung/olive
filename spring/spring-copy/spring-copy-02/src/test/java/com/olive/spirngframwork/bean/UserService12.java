package com.olive.spirngframwork.bean;

import java.util.Random;

/**
 * 类UserService12的实现描述：UserService12
 *
 * @author dongtangqiang 2022/7/9 17:00
 */
public class UserService12 implements IUserService{

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
