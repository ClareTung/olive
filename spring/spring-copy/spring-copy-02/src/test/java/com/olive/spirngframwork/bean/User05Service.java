package com.olive.spirngframwork.bean;

/**
 * 类User05Service的实现描述：User05Service
 *
 * @author dongtangqiang 2022/5/28 23:02
 */
public class User05Service {

    private String uId;

    private UserDao userDao;

    public String queryUserInfo() {
        System.out.println("进入User05Service.queryUserInfo()...");
        return userDao.queryUserName(uId);
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
