package com.fish.dao.fourth.model;

import java.util.Date;

public class WxDailyDetain {
    private Integer id;

    private String appId;

    private String refDate;

    private String dataType;

    private Integer day0;

    private Integer day1;

    private Integer day2;

    private Integer day3;

    private Integer day4;

    private Integer day5;

    private Integer day6;

    private Integer day7;

    private Integer day14;

    private Integer day30;

    private Date insertTime;

    /**
     * 展示数据--产品名称
     */
    private String appName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate == null ? null : refDate.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public Integer getDay0() {
        return day0;
    }

    public void setDay0(Integer day0) {
        this.day0 = day0;
    }

    public Integer getDay1() {
        return day1;
    }

    public void setDay1(Integer day1) {
        this.day1 = day1;
    }

    public Integer getDay2() {
        return day2;
    }

    public void setDay2(Integer day2) {
        this.day2 = day2;
    }

    public Integer getDay3() {
        return day3;
    }

    public void setDay3(Integer day3) {
        this.day3 = day3;
    }

    public Integer getDay4() {
        return day4;
    }

    public void setDay4(Integer day4) {
        this.day4 = day4;
    }

    public Integer getDay5() {
        return day5;
    }

    public void setDay5(Integer day5) {
        this.day5 = day5;
    }

    public Integer getDay6() {
        return day6;
    }

    public void setDay6(Integer day6) {
        this.day6 = day6;
    }

    public Integer getDay7() {
        return day7;
    }

    public void setDay7(Integer day7) {
        this.day7 = day7;
    }

    public Integer getDay14() {
        return day14;
    }

    public void setDay14(Integer day14) {
        this.day14 = day14;
    }

    public Integer getDay30() {
        return day30;
    }

    public void setDay30(Integer day30) {
        this.day30 = day30;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appId=").append(appId);
        sb.append(", refDate=").append(refDate);
        sb.append(", dataType=").append(dataType);
        sb.append(", day0=").append(day0);
        sb.append(", day1=").append(day1);
        sb.append(", day2=").append(day2);
        sb.append(", day3=").append(day3);
        sb.append(", day4=").append(day4);
        sb.append(", day5=").append(day5);
        sb.append(", day6=").append(day6);
        sb.append(", day7=").append(day7);
        sb.append(", day14=").append(day14);
        sb.append(", day30=").append(day30);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}