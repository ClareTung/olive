package com.olive.es.start.controller.base;

import com.olive.es.start.service.base.IndexService2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "基础操作-索引操作")
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService2 indexService;

    @ApiOperation(value = "验证索引是否存在", notes = "验证索引是否存在")
    @GetMapping("/exist")
    public Object existIndex() {
        return indexService.existsIndex();
    }

    @ApiOperation(value = "新增索引", notes = "新增加索引信息")
    @PostMapping
    public Object createIndex() {
        return indexService.createIndex();
    }

    @ApiOperation(value = "删除索引", notes = "删除索引信息")
    @DeleteMapping
    public Object deleteIndex() {
        return indexService.deleteIndex();
    }

}
