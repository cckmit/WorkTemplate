package com.cc.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:38
 */
@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "config")
public class BaseConfig {

    /**
     * 用户登录超时时间
     */
    private int timeout;

}
