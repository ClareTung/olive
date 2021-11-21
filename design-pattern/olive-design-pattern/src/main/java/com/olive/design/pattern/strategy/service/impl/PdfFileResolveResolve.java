package com.olive.design.pattern.strategy.service.impl;

import com.olive.design.pattern.strategy.enums.FileResolveTypeEnum;
import com.olive.design.pattern.strategy.service.IFileResolveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * PDF文件解析
 *
 * @author dongtangqiang
 */
@Slf4j
@Service
public class PdfFileResolveResolve implements IFileResolveStrategy {

    @Override
    public FileResolveTypeEnum obtainFileType() {
        return FileResolveTypeEnum.PDF;
    }

    @Override
    public void resolve(Object params) {
        log.info("pdf文件类型解析，入参：{}", params);
        // 具体执行解析逻辑
    }
}
