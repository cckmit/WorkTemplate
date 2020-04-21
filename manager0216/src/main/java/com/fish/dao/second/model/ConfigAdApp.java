package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 微信广告配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdApp {

    private String ddAppId;
    private String ddMinVersion;
    private int ddCombinationId;
    private boolean ddAllowedShow;
    private String ddWxBannerUnit;
    private int ddWxBannerTime;
    private boolean ddWxBannerAllowedShow;
    private int ddWxBannerStrategyId;
    private String ddWxBannerStrategyValue;
    private String ddWxIntUint;
    private boolean ddWxIntAllowedShow;
    private int ddWxIntStrategyId;
    private String ddWxIntStrategyValue;
    private String ddWxReVideoUnit;
    private boolean ddWxReVideoAllowedShow;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    // 以下是展示参数
    /**
     * App名称
     */
    private String appName;
    /**
     * 广告组合名称
     */
    private String combinationName;
    /**
     * 微信Banner广告策略
     */
    private String wxBannerStrategyName;
    /**
     * 微信插屏广告策略
     */
    private String wxIntStrategyName;


    public String getDdAppId() {
        return ddAppId;
    }

    public void setDdAppId(String ddAppId) {
        this.ddAppId = ddAppId;
    }

    public String getDdMinVersion() {
        return ddMinVersion;
    }

    public void setDdMinVersion(String ddMinVersion) {
        this.ddMinVersion = ddMinVersion;
    }

    public int getDdCombinationId() {
        return ddCombinationId;
    }

    public void setDdCombinationId(int ddCombinationId) {
        this.ddCombinationId = ddCombinationId;
    }

    public boolean isDdAllowedShow() {
        return ddAllowedShow;
    }

    public void setDdAllowedShow(boolean ddAllowedShow) {
        this.ddAllowedShow = ddAllowedShow;
    }

    public String getDdWxBannerUnit() {
        return ddWxBannerUnit;
    }

    public void setDdWxBannerUnit(String ddWxBannerUnit) {
        this.ddWxBannerUnit = ddWxBannerUnit;
    }

    public int getDdWxBannerTime() {
        return ddWxBannerTime;
    }

    public void setDdWxBannerTime(int ddWxBannerTime) {
        this.ddWxBannerTime = ddWxBannerTime;
    }

    public boolean isDdWxBannerAllowedShow() {
        return ddWxBannerAllowedShow;
    }

    public void setDdWxBannerAllowedShow(boolean ddWxBannerAllowedShow) {
        this.ddWxBannerAllowedShow = ddWxBannerAllowedShow;
    }

    public int getDdWxBannerStrategyId() {
        return ddWxBannerStrategyId;
    }

    public void setDdWxBannerStrategyId(int ddWxBannerStrategyId) {
        this.ddWxBannerStrategyId = ddWxBannerStrategyId;
    }

    public String getDdWxBannerStrategyValue() {
        return ddWxBannerStrategyValue;
    }

    public void setDdWxBannerStrategyValue(String ddWxBannerStrategyValue) {
        this.ddWxBannerStrategyValue = ddWxBannerStrategyValue;
    }

    public String getDdWxIntUint() {
        return ddWxIntUint;
    }

    public void setDdWxIntUint(String ddWxIntUint) {
        this.ddWxIntUint = ddWxIntUint;
    }

    public boolean isDdWxIntAllowedShow() {
        return ddWxIntAllowedShow;
    }

    public void setDdWxIntAllowedShow(boolean ddWxIntAllowedShow) {
        this.ddWxIntAllowedShow = ddWxIntAllowedShow;
    }

    public int getDdWxIntStrategyId() {
        return ddWxIntStrategyId;
    }

    public void setDdWxIntStrategyId(int ddWxIntStrategyId) {
        this.ddWxIntStrategyId = ddWxIntStrategyId;
    }

    public String getDdWxIntStrategyValue() {
        return ddWxIntStrategyValue;
    }

    public void setDdWxIntStrategyValue(String ddWxIntStrategyValue) {
        this.ddWxIntStrategyValue = ddWxIntStrategyValue;
    }

    public String getDdWxReVideoUnit() {
        return ddWxReVideoUnit;
    }

    public void setDdWxReVideoUnit(String ddWxReVideoUnit) {
        this.ddWxReVideoUnit = ddWxReVideoUnit;
    }

    public boolean isDdWxReVideoAllowedShow() {
        return ddWxReVideoAllowedShow;
    }

    public void setDdWxReVideoAllowedShow(boolean ddWxReVideoAllowedShow) {
        this.ddWxReVideoAllowedShow = ddWxReVideoAllowedShow;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCombinationName() {
        return combinationName;
    }

    public void setCombinationName(String combinationName) {
        this.combinationName = combinationName;
    }

    public String getWxBannerStrategyName() {
        return wxBannerStrategyName;
    }

    public void setWxBannerStrategyName(String wxBannerStrategyName) {
        this.wxBannerStrategyName = wxBannerStrategyName;
    }

    public String getWxIntStrategyName() {
        return wxIntStrategyName;
    }

    public void setWxIntStrategyName(String wxIntStrategyName) {
        this.wxIntStrategyName = wxIntStrategyName;
    }
}
