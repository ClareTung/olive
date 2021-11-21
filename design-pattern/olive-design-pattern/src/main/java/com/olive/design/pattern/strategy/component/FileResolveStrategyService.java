package com.olive.design.pattern.strategy.component;

import com.olive.design.pattern.strategy.enums.FileResolveTypeEnum;
import com.olive.design.pattern.strategy.service.IFileResolveStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用策略模式
 *
 * @author dongtangqiang
 */
@Component
public class FileResolveStrategyService implements ApplicationContextAware {

    Map<FileResolveTypeEnum, IFileResolveStrategy> fileResolveServiceMap = new ConcurrentHashMap<>();

    /**
     * 实际调用的解析方法
     *
     * @param fileResolveTypeEnum
     * @param params
     */
    public void resolveFile(FileResolveTypeEnum fileResolveTypeEnum, Object params) {
        IFileResolveStrategy fileResolveService = fileResolveServiceMap.get(fileResolveTypeEnum);
        if (fileResolveService != null) {
            fileResolveService.resolve(params);
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IFileResolveStrategy> actualFileResolveServiceMap = applicationContext.getBeansOfType(IFileResolveStrategy.class);
        actualFileResolveServiceMap.values().forEach(fileResolveService -> fileResolveServiceMap.put(fileResolveService.obtainFileType(), fileResolveService));
    }
}
