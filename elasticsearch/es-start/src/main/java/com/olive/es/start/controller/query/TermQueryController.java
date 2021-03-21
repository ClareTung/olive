package com.olive.es.start.controller.query;

import com.olive.es.start.service.query.TermQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "查询-精确查询")
@RestController
@RequestMapping("/query")
public class TermQueryController {

    @Autowired
    private TermQueryService termQueryService;

    @ApiOperation(value = "精确查询", notes = "精确查询")
    @GetMapping("/term")
    public Object termQuery() {
        return termQueryService.termQuery();
    }

    @ApiOperation(value = "精确查询一个字段多个值", notes = "精确查询一个字段多个值")
    @GetMapping("/terms")
    public Object termsQuery() {
        return termQueryService.termsQuery();
    }

}
