package com.blazefire.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 从application.yml读取配置文件
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-09 21:09
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@Data
public class BaseConfig {

    private String adLogSaveDir;
    private String adLogSaveDir2;
    private String desKey1;
    private String desKey2;
    private int serverId;
    private int userTableSingleLimit;
    private String noticeUrlJson;
    private String appInfoUrl;
    private String logDownloadUrl;
    private String logDownloadUrl2;
    private String wxNoticeUrl;


}
