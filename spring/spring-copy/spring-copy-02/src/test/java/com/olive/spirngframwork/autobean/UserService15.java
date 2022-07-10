package com.olive.spirngframwork.autobean;

import java.util.Random;

import com.olive.spirngframwork.bean.IUserService;
import com.olive.springframwork.beans.factory.anotation.Autowired;
import com.olive.springframwork.beans.factory.anotation.Value;
import com.olive.springframwork.stereotype.Component;

/**
 * 类UserService15的实现描述：UserService15
 *
 * @author dongtangqiang 2022/7/10 14:58
 */
@Component
public class UserService15 implements IUserService {

    @Value("${token}")
    private String token;

    @Autowired
    private UserDao15 userDao;

    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userDao.queryUserName("10001") + "，" + token;
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}