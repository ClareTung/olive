package com.olive.spirngframwork.bean;

/**
 * 类IUserService的实现描述：IUserService
 *
 * @author dongtangqiang 2022/7/9 16:59
 */
public interface IUserService {

    String queryUserInfo();

    String register(String userName);
}
