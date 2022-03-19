package com.olive.springboot.start.model;

import com.olive.springboot.start.annotation.NeedSetValue;
import com.olive.springboot.start.service.UserServiceImpl;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单
 *
 * @author dongtangqiang
 */
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = -8755898624890756947L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long customerId;

    /**
     * 用户姓名
     */
    @NeedSetValue(beanClass = UserServiceImpl.class, method = "findById", param = "customerId", targetField = "name")
    private String customerName;

}
