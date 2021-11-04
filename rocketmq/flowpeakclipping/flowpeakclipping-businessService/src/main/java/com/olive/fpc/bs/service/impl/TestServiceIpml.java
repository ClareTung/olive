package com.olive.fpc.bs.service.impl;

import com.olive.fpc.bs.entity.TestBean;
import com.olive.fpc.bs.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 18:21
 */
@Service
public class TestServiceIpml implements TestService {
    @Override
    public String operation(TestBean testBean) {
        return testBean.getMessage();
    }
}
