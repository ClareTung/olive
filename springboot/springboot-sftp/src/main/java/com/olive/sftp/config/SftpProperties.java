package com.olive.sftp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author dongtangqiang
 */
@Data
@Component
@ConfigurationProperties(ignoreInvalidFields = false, prefix = "sftp.client")
public class SftpProperties {

    private String host;

    private Integer port;

    private String protocol;

    private String userName;

    private String password;

    private String root;

    private String privateKey;

    private String passphrase;

    private String sessionStrictHostKeyChecking;

    private Integer sessionConnectTimeout;

    private Integer channelConnectedTimeout;

}
