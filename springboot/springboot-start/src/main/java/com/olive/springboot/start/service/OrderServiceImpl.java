package com.olive.springboot.start.service;

import com.olive.springboot.start.annotation.NeedSetFieldValue;
import com.olive.springboot.start.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务
 *
 * @author dongtangqiang
 */
@Service
public class OrderServiceImpl {

    /**
     * 获取指定用户的订单
     *
     * @param customerId
     * @return
     */
    @NeedSetFieldValue
    public List<Order> findByUserId(Long customerId) {
        List<Order> result = new ArrayList<>();
        result.add(obtainOneOrder(customerId));

        return result;
    }

    /**
     * 模拟数据库返回
     *
     * @param customerId
     * @return
     */
    private Order obtainOneOrder(Long customerId) {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId(1000L);

        return order;
    }
}
