package com.olive.design.pattern.strategy.service.impl;

import com.olive.design.pattern.strategy.enums.FileResolveTypeEnum;
import com.olive.design.pattern.strategy.service.IFileResolveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author dongtangqiang
 */
@Slf4j
@Service
public class DefaultFileResolveResolve implements IFileResolveStrategy {

    @Override
    public FileResolveTypeEnum obtainFileType() {
        return FileResolveTypeEnum.DEFAULT;
    }

    @Override
    public void resolve(Object params) {
        log.info("默认文件类型解析，入参：{}", params);
        // 具体执行解析逻辑
    }
}