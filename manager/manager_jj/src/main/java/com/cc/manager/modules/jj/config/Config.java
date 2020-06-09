package com.cc.manager.modules.jj.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 街机订单相关配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-15 17:28
 */
@Component
@Configuration
@PropertySource("classpath:module-wx.properties")
@Data
public class Config {
    /**
     * 微信证书信息
     */
    @Value("${pkcs12}")
    public String P12;
    /**
     * 微信证书密码
     */
    @Value("${p12password}")
    public String PASSWORD;
    /**
     * 设备号 ,选填
     */
    @Value("${device_info}")
    public String DEVICE_INFO;
    /**
     * 校验用户姓名选项 NO_CHECK：不校验真实姓名,FORCE_CHECK：强校验真实姓名,OPTION_CHECK：针对已实名认证的用户才校验真实姓名
     */
    @Value("${check_name}")
    public CheckNameType CHECK_NAME;
    /**
     * 企业付款描述信息
     */
    @Value("${desc}")
    public String DESC;
    /**
     * 调用接口的机器IP地址
     */
    @Value("${spbill_create_ip}")
    public String SPBILL_CREATE_IP;
    /**
     * 微信KEY值
     */
    @Value("${key}")
    public String KEY;
    /**
     * 支付到零钱接口
     */
    @Value("${transfers_url}")
    public String TRANSFERS_URL;
    /**
     * 统一下单接口
     */
    @Value("${unifiedorder_url}")
    public String UNIFIEDORDER_URL;
    /**
     * 订单查询接口
     */
    @Value("${orderquery_url}")
    public String ORDERQUERY_URL;
    /**
     * 商户号
     */
    @Value("${mchid}")
    public String MCHID;

    /**
     * 校验用户姓名选项
     */
    public enum CheckNameType {
        NO_CHECK, // 不校验真实姓名
        FORCE_CHECK, // 强校验真实姓名
        OPTION_CHECK// 针对已实名认证的用户才校验真实姓名
    }
}
