package com.cc.manager.modules.jj.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 街机业务相关配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-15 17:28
 */
@Component
@Configuration
@PropertySource("classpath:module-jj.properties")
@Data
public class JjConfig {
    /**
     *
     */
    @Value("${upload}")
    private String upload;
    /**
     * domain
     */
    @Value("${domain}")
    private String domain;
    /**
     * excel上传地址
     */
    @Value("${excelSave}")
    private String excelSave;
    /**
     * 资源路径
     */
    @Value("${readRes}")
    private String readRes;
    /**
     * 赛场资源
     */
    @Value("${matchRes}")
    private String matchRes;
    /**
     * 资源域名
     */
    @Value("${resHost}")
    private String resHost;
    /**
     * 刷新接口
     */
    @Value("${flushCache}")
    private String flushCache;
    /**
     * 刷新公众号接口
     */
    @Value("${flushPublicCache}")
    private String flushPublicCache;
    /**
     * 刷新在线人数
     */
    @Value("${flushOnline}")
    private String flushOnline;
    /**
     * 上传配置
     */
    @Value("#{T(com.alibaba.fastjson.JSONObject).parseObject('${uploadJson}')}")
    private JSONObject uploadJson;
    /**
     * 微信查询订单地址
     */
    @Value("${wxQueryUrl}")
    private String wxQueryUrl;
    /**
     * 服务器补单地址
     */
    @Value("${supplementUrl}")
    private String supplementUrl;
}
