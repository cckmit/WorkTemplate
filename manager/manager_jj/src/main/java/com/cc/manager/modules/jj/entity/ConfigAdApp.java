package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 微信广告配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_app")
public class ConfigAdApp implements BaseCrudEntity<ConfigAdApp> {

    @TableField(value = "ddAppId")
    private String appId;

    @TableField(value = "ddMinVersion")
    private String minVersion;

    @TableField(value = "ddCombinationId")
    private int combinationId;

    @TableField(value = "ddAllowedShow")
    private boolean allowedShow;

    @TableField(value = "ddWxBannerUnit")
    private String wxBannerUnit;

    @TableField(value = "ddWxBannerTime")
    private int wxBannerTime;

    @TableField(value = "ddWxBannerAllowedShow")
    private boolean wxBannerAllowedShow;

    @TableField(value = "ddWxBannerStrategyId")
    private int wxBannerStrategyId;

    @TableField(value = "ddWxBannerStrategyValue")
    private String wxBannerStrategyValue;

    @TableField(value = "ddWxIntUint")
    private String wxIntUint;

    @TableField(value = "ddWxIntAllowedShow")
    private boolean wxIntAllowedShow;

    @TableField(value = "ddWxIntStrategyId")
    private int wxIntStrategyId;

    @TableField(value = "ddWxIntStrategyValue")
    private String wxIntStrategyValue;

    @TableField(value = "ddWxReVideoUnit")
    private String wxReVideoUnit;

    @TableField(value = "ddWxReVideoAllowedShow")
    private boolean wxReVideoAllowedShow;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * App名称
     */
    @TableField(exist = false)
    private String appName;
    /**
     * 广告组合名称
     */
    @TableField(exist = false)
    private String combinationName;
    /**
     * 微信Banner广告策略
     */
    @TableField(exist = false)
    private String wxBannerStrategyName;
    /**
     * 微信插屏广告策略
     */
    @TableField(exist = false)
    private String wxIntStrategyName;

}
