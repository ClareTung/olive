package com.olive.springboot.start.controller;

import com.google.common.collect.Lists;
import com.olive.springboot.start.vo.resp.UserRespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @desc: 用户服务
 * @classname: UserController
 * @author: dongtangqiang
 * @date: 2020-12-31
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping(value = "/info/get")
    public List<UserRespVO> getUserInfo() {
        List<UserRespVO> result = Lists.newArrayList();

        result.add(UserRespVO.builder().id(1000L)
                .userName("clare")
                .userNick("tang tang")
                .hobby(Lists.newArrayList("ride", "swim"))
                .build());

        result.add(UserRespVO.builder().build());


        return result;
    }

}
