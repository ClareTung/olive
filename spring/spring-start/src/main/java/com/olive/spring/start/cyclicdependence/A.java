package com.olive.spring.start.cyclicdependence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 循环依赖A
 * @program: olive
 * @author: dtq
 * @create: 2021/9/22 16:56
 */
@Component
public class A {

    @Autowired
    private B b;
}
