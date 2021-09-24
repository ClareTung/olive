package com.olive.java.start.proxy;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/9/24 13:56
 */
public class UserServiceImpl implements IUserService {
    @Override
    public String queryUserInfo() {
        return "I'm Clare Tung";
    }
}
