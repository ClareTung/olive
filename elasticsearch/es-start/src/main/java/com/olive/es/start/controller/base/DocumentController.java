package com.olive.es.start.controller.base;

import com.olive.es.start.service.base.DocumentService2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "基础操作-文档操作")
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService2 documentService;

    @ApiOperation(value = "获取文档", notes = "获取某个文档信息")
    @GetMapping
    public Object getDocument() {
        return documentService.getDocument();
    }

    @ApiOperation(value = "验证文档是否存在", notes = "验证某个文档是否存在")
    @GetMapping("/exist")
    public Object existDocument() {
        return documentService.existsDocument();
    }

    @ApiOperation(value = "新增文档", notes = "新增一个文档")
    @PostMapping
    public Object addDocument() {
        return documentService.addDocument();
    }

    @ApiOperation(value = "更新文档", notes = "更新一个文档信息")
    @PutMapping
    public Object updateDocument() {
        return documentService.updateDocument();
    }

    @ApiOperation(value = "删除文档", notes = "删除一个文档信息")
    @DeleteMapping
    public Object deleteDocument() {
        return documentService.deleteDocument();
    }

}
