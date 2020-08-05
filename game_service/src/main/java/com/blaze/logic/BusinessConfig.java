package com.blaze.logic;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 业务逻辑配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-10 12:37
 */
@Data
@Configuration
@PropertySource("classpath:businessConfig.properties")
public class BusinessConfig {
    /**
     * 头条登录接口
     */
    @Value("${tt_login_url}")
    private String ttLoginUrl;
    /**
     * 微信登录接口
     */
    @Value("${wx_login_url}")
    private String wxLoginUrl;

}
