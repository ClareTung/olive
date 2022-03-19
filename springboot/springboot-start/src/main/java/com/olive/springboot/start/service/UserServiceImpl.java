package com.olive.springboot.start.service;

import com.olive.springboot.start.model.User;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author dongtangqiang
 */
@Service
public class UserServiceImpl {


    /**
     * 根据ID获取用户
     *
     * @param id
     * @return
     */
    public User findById(Long id) {
        User user = new User();
        user.setId(1000L);
        user.setName("ClareTung");
        return user;
    }
}
