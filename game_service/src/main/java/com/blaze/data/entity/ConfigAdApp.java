package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信广告配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
@Data
@TableName(schema = "persie", value = "config_ad_app")
public class ConfigAdApp implements BaseEntity<ConfigAdApp> {

    @TableField(value = "ddAppId")
    private String appId;

    @TableField(value = "ddMinVersion")
    private String minVersion;

    @TableField(value = "ddContentPoolId")
    private int contentPoolId;

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

    @Override
    public Serializable getCacheKey() {
        return this.appId + "_" + this.minVersion;
    }

}
