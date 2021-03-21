package com.olive.starter.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/2/4 16:51
 */
@Data
@ConfigurationProperties("olive")
public class OliveProperties {
    /**
     * olive open
     */
    boolean enable = false;

    /**
     * userId
     */
    private String userId;
}
