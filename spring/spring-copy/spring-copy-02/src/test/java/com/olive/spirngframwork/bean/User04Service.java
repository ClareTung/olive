package com.olive.spirngframwork.bean;

/**
 * 类User04Service的实现描述：User04Service
 *
 * @author dongtangqiang 2022/5/28 18:09
 */
public class User04Service {

    private String uId;

    private UserDao userDao;

    public void queryUserInfo() {
        System.out.println("查询用户信息：" + userDao.queryUserName(uId));
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
