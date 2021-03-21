package com.olive.sharding.jdbc.start.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.olive.sharding.jdbc.start.model.TOrder;
import com.olive.sharding.jdbc.start.model.TOrderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface OrderRepository extends BaseMapper<TOrder> {


    List<TOrderDto> selectOrderAndItemByOrderId(TOrderDto tOrder);

    List<TOrderDto> selectOrderListPage(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("orderId") Long orderId);

    List<TOrderDto> selectOrderListPage();
}

