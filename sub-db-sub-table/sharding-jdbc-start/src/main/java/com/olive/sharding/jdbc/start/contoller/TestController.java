package com.olive.sharding.jdbc.start.contoller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.olive.sharding.jdbc.start.mapper.ConfigRepository;
import com.olive.sharding.jdbc.start.mapper.OrderItemRepository;
import com.olive.sharding.jdbc.start.mapper.OrderRepository;
import com.olive.sharding.jdbc.start.model.TConfig;
import com.olive.sharding.jdbc.start.model.TOrder;
import com.olive.sharding.jdbc.start.model.TOrderDto;
import com.olive.sharding.jdbc.start.model.TOrderItem;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sharding")
public class TestController {

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderItemRepository orderItemRepository;

    @Resource
    private ConfigRepository configRepository;

    @GetMapping(value = "/insertOrder")
    public String insertOrder() {
        for (int i = 0; i < 5; i++) {
            /*// 清除掉上一次的规则，否则会报错
            HintManager.clear();
            // HintManager API 工具类实例
            HintManager hintManager = HintManager.getInstance();
            // 直接指定对应具体的数据库
            hintManager.addDatabaseShardingValue("ds", 0);
            // 设置表的分片健
            hintManager.addTableShardingValue("t_order", 0);
            hintManager.addTableShardingValue("t_order", 1);
            hintManager.addTableShardingValue("t_order", 2);

            // 在读写分离数据库中，Hint 可以强制读主库
            hintManager.setMasterRouteOnly();*/

            TOrder order = new TOrder();
            order.setOrderNo("A000" + i);
            order.setCreateName("订单 " + i);
            order.setUserId(Long.parseLong(i + ""));
            order.setPrice(new BigDecimal("" + i));
            orderRepository.insert(order);

//            TOrderItem orderItem = new TOrderItem();
//            orderItem.setOrderId(order.getOrderId());
//            orderItem.setOrderNo("A000" + i);
//            orderItem.setItemName("服务项目" + i);
//            orderItem.setPrice(new BigDecimal("" + i));
//            orderItemRepository.insert(orderItem);
        }
        return "success";
    }

    @GetMapping(value = "/insertOrderItem")
    public String insertOrderItem() {
        List<TOrderDto> orderDtoList = orderRepository.selectOrderListPage();

        for (TOrderDto tOrderDto : orderDtoList) {
            TOrderItem orderItem = new TOrderItem();
            orderItem.setOrderId(tOrderDto.getOrderId());

            String i = tOrderDto.getOrderNo().substring(tOrderDto.getOrderNo().length() - 1, tOrderDto.getOrderNo().length());

            orderItem.setOrderNo("A000" + i);
            orderItem.setItemName("服务项目" + i);
            orderItem.setPrice(new BigDecimal("" + i));
            orderItemRepository.insert(orderItem);
        }
        return "success";
    }


    @GetMapping(value = "/config")
    public String config() {

        TConfig tConfig = new TConfig();
        tConfig.setRemark("我是广播表");
        tConfig.setCreateTime(new Date());
        tConfig.setLastModifyTime(new Date());
        configRepository.insert(tConfig);
        return "success";
    }


    @GetMapping(value = "/getOneOrder")
    public String getOne(String orderId) {
        return JSON.toJSONString(orderRepository.selectById(Long.parseLong(orderId)));
    }


    @GetMapping(value = "/selectOrderAndItemByOrderId")
    public String selectOrderAndItemByOrderId(String orderId) {
        TOrderDto tOrder = new TOrderDto();
        if (!StringUtils.isEmpty(orderId)) {
            tOrder.setOrderId(Long.parseLong(orderId));
        }
        return JSON.toJSONString(orderRepository.selectOrderAndItemByOrderId(tOrder));
    }

    @GetMapping("/orderList")
    public Object list() {
        return orderRepository.selectList(new QueryWrapper<>());
    }

    @GetMapping(value = "/selectOrderListPage")
    public List<TOrderDto> selectOrderListPage() {
        return orderRepository.selectOrderListPage();
    }

    public static void main(String[] args) {

    }
}
