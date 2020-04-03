package com.fish.dao.fourth.model;

import java.math.BigDecimal;

public class AdValueWxAdpos {
    private Integer id;

    private String date;

    private String appId;

    private Integer clickCount;

    private BigDecimal clickRate;

    private BigDecimal ecpm;

    private Integer exposureCount;

    private BigDecimal exposureRate;

    private Integer income;

    private Integer reqSuccCount;

    private String slotId;

    /**
     * 展示数据展品名称
     *
     * @return
     */
    private String appName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public BigDecimal getClickRate() {
        return clickRate;
    }

    public void setClickRate(BigDecimal clickRate) {
        this.clickRate = clickRate;
    }

    public BigDecimal getEcpm() {
        return ecpm;
    }

    public void setEcpm(BigDecimal ecpm) {
        this.ecpm = ecpm;
    }

    public Integer getExposureCount() {
        return exposureCount;
    }

    public void setExposureCount(Integer exposureCount) {
        this.exposureCount = exposureCount;
    }

    public BigDecimal getExposureRate() {
        return exposureRate;
    }

    public void setExposureRate(BigDecimal exposureRate) {
        this.exposureRate = exposureRate;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getReqSuccCount() {
        return reqSuccCount;
    }

    public void setReqSuccCount(Integer reqSuccCount) {
        this.reqSuccCount = reqSuccCount;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId == null ? null : slotId.trim();
    }

    public String getAppName() { return appName; }

    public void setAppName(String appName) { this.appName = appName; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", appId=").append(appId);
        sb.append(", clickCount=").append(clickCount);
        sb.append(", clickRate=").append(clickRate);
        sb.append(", ecpm=").append(ecpm);
        sb.append(", exposureCount=").append(exposureCount);
        sb.append(", exposureRate=").append(exposureRate);
        sb.append(", income=").append(income);
        sb.append(", reqSuccCount=").append(reqSuccCount);
        sb.append(", slotId=").append(slotId);
        sb.append("]");
        return sb.toString();
    }
}