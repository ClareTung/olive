package com.olive.starter.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/4 17:40
 */
@Data
@ConfigurationProperties("aspectLog")
public class AspectLogProperties {
    private boolean enable;
}
