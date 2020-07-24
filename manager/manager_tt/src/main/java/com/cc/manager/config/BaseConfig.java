package com.cc.manager.config;

import com.alibaba.fastjson.JSONObject;
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
    private String upload;

    private String domain;

    private String excelSave;

    //资源路径
    private String readRes;
    //赛场资源
    private String matchRes;
    //资源域名
    private String resHost;
    //刷新接口
    private String flushCache;
    //刷新公众号接口
    private String flushPublicCache;
    //刷新在线人数
    private String flushOnline;

    private JSONObject uploadJson;
    //微信查询订单地址
    private String wxQueryUrl;
    /**
     * 服务器补单地址
     */
    private String supplementUrl;

}
