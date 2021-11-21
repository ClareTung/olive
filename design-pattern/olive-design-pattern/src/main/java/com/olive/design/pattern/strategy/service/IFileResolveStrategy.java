package com.olive.design.pattern.strategy.service;

import com.olive.design.pattern.strategy.enums.FileResolveTypeEnum;

/**
 * @author dongtangqiang
 */
public interface IFileResolveStrategy {

    /**
     * 获取文件类型
     *
     * @return
     */
    FileResolveTypeEnum obtainFileType();

    /**
     * 公共的解析方法
     *
     * @param params
     */
    void resolve(Object params);
}
