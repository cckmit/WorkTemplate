package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 微信广告配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdWx {

    private int ddId;
    private String ddName;
    private String ddBannerUnit;
    private int ddBannerTime;
    private boolean ddBannerAllowedShow;
    private int ddBannerStrategyId;
    private String ddBannerStrategyValue;
    private String ddIntUint;
    private boolean ddIntAllowedShow;
    private int ddIntStrategyId;
    private String ddIntStrategyValue;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    // 以下是展示参数
    /**
     * 微信Banner广告策略
     */
    private String ddBannerStrategyName;
    /**
     * 微信插屏广告策略
     */
    private String ddIntStrategyName;

    public int getDdId() { return ddId; }

    public void setDdId(int ddId) { this.ddId = ddId; }

    public String getDdName() { return ddName; }

    public void setDdName(String ddName) { this.ddName = ddName; }

    public String getDdBannerUnit() { return ddBannerUnit; }

    public void setDdBannerUnit(String ddBannerUnit) { this.ddBannerUnit = ddBannerUnit; }

    public int getDdBannerTime() { return ddBannerTime; }

    public void setDdBannerTime(int ddBannerTime) { this.ddBannerTime = ddBannerTime; }

    public boolean isDdBannerAllowedShow() { return ddBannerAllowedShow; }

    public void setDdBannerAllowedShow(boolean ddBannerAllowedShow) { this.ddBannerAllowedShow = ddBannerAllowedShow; }

    public int getDdBannerStrategyId() { return ddBannerStrategyId; }

    public void setDdBannerStrategyId(int ddBannerStrategyId) { this.ddBannerStrategyId = ddBannerStrategyId; }

    public String getDdBannerStrategyValue() { return ddBannerStrategyValue; }

    public void setDdBannerStrategyValue(String ddBannerStrategyValue) {
        this.ddBannerStrategyValue = ddBannerStrategyValue;
    }

    public String getDdIntUint() { return ddIntUint; }

    public void setDdIntUint(String ddIntUint) { this.ddIntUint = ddIntUint; }

    public boolean isDdIntAllowedShow() { return ddIntAllowedShow; }

    public void setDdIntAllowedShow(boolean ddIntAllowedShow) { this.ddIntAllowedShow = ddIntAllowedShow; }

    public int getDdIntStrategyId() { return ddIntStrategyId; }

    public void setDdIntStrategyId(int ddIntStrategyId) { this.ddIntStrategyId = ddIntStrategyId; }

    public String getDdIntStrategyValue() { return ddIntStrategyValue; }

    public void setDdIntStrategyValue(String ddIntStrategyValue) { this.ddIntStrategyValue = ddIntStrategyValue; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getDdBannerStrategyName() { return ddBannerStrategyName; }

    public void setDdBannerStrategyName(
            String ddBannerStrategyName) { this.ddBannerStrategyName = ddBannerStrategyName; }

    public String getDdIntStrategyName() { return ddIntStrategyName; }

    public void setDdIntStrategyName(String ddIntStrategyName) { this.ddIntStrategyName = ddIntStrategyName; }

}
