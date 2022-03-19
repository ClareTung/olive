package com.olive.springboot.start.controller;

import com.olive.springboot.start.model.Order;
import com.olive.springboot.start.service.OrderServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单服务
 *
 * @author dongtangqiang
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderServiceImpl orderService;

    @GetMapping(value = "/getByUserId")
    public List<Order> getByUserId() {
        return orderService.findByUserId(1000L);
    }
}
