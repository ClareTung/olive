package com.olive.spirngframwork.bean;

import com.olive.springframwork.beans.factory.DisposableBean;
import com.olive.springframwork.beans.factory.InitializingBean;

/**
 * 类User07Service的实现描述：User07Service
 *
 * @author dongtangqiang 2022/6/4 20:41
 */
public class User07Service implements InitializingBean, DisposableBean {
    private String uId;
    private String company;
    private String location;
    private User07Dao userDao;

    public String queryUserInfo() {
        return userDao.queryUserName(uId);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行：User07Service.destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行：User07Service.afterPropertiesSet");
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User07Dao getUserDao() {
        return userDao;
    }

    public void setUserDao(User07Dao userDao) {
        this.userDao = userDao;
    }
}
