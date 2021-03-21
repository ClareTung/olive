package com.olive.sharding.jdbc.start.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.olive.sharding.jdbc.start.model.TConfig;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ConfigRepository extends BaseMapper<TConfig> {

}

