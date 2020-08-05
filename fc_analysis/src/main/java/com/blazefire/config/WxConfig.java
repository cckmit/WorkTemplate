package com.blazefire.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 微信 相关配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 16:02
 */
@Data
@Configuration
@PropertySource(value = "classpath:wx.properties", encoding = "UTF-8")
public class WxConfig {

    /**
     * 从街机正式服务器获取accessToken地址
     */
    @Value("${appInfoUrl}")
    private String appInfoUrl;
    /**
     * 广告分析接口
     */
    @Value("${adDataUrl}")
    private String adDataUrl;
    /**
     * 广告位枚举值
     */
    @Value("#{T(com.alibaba.fastjson.JSONObject).parseObject('${adSoitJson}')}")
    private JSONObject adSoitJson;
    /**
     * 获取用户访问小程序日留存地址
     */
    @Value("${dailyRetainUrl}")
    private String dailyRetainUrl;
    /**
     * 获取用户访问小程序月留存地址
     */
    @Value("${monthlyRetainUrl}")
    private String monthlyRetainUrl;
    /**
     * 获取用户访问小程序周留存地址
     */
    @Value("${weeklyRetainUrl}")
    private String weeklyRetainUrl;
    /**
     * 获取用户访问小程序数据概况地址
     */
    @Value("${dailySummaryUrl}")
    private String dailySummaryUrl;
    /**
     * 获取用户访问小程序数据日趋势地址
     */
    @Value("${dailyVisitTrendUrl}")
    private String dailyVisitTrendUrl;
    /**
     * 获取用户访问小程序数据月趋势地址
     */
    @Value("${monthlyVisitTrendUrl}")
    private String monthlyVisitTrendUrl;
    /**
     * 获取用户访问小程序数据周趋势地址
     */
    @Value("${weeklyVisitTrendUrl}")
    private String weeklyVisitTrendUrl;
    /**
     * 获取小程序新增或活跃用户的画像分布数据地址
     */
    @Value("${userPortraitUrl}")
    private String userPortraitUrl;
    /**
     * 获取用户小程序访问分布数据地址
     */
    @Value("${visitDistributionUrl}")
    private String visitDistributionUrl;
    /**
     * 获取访问页面地址
     */
    @Value("${visitPageUrl}")
    private String visitPageUrl;
    /**
     * 微信小程序小游戏公告提醒关键字
     */
    @Value("#{T(com.alibaba.fastjson.JSONArray).parseArray('${wxNoticeKeywords}')}")
    private JSONArray wxNoticeKeywords;

}
