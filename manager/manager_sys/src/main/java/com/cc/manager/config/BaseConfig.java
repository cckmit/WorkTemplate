package com.cc.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:38
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "config")
public class BaseConfig {

}
