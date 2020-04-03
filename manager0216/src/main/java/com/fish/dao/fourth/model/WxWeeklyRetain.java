package com.fish.dao.fourth.model;

import java.util.Date;

public class WxWeeklyRetain {
    private Integer id;

    private String appId;

    private String refWeek;

    private String dataType;

    private Integer week0;

    private Integer week1;

    private Integer week2;

    private Integer week3;

    private Integer week4;

    private Integer week5;

    private Integer week6;

    private Integer week7;

    private Date insertTime;

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

    public String getRefWeek() {
        return refWeek;
    }

    public void setRefWeek(String refWeek) {
        this.refWeek = refWeek == null ? null : refWeek.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public Integer getWeek0() {
        return week0;
    }

    public void setWeek0(Integer week0) {
        this.week0 = week0;
    }

    public Integer getWeek1() {
        return week1;
    }

    public void setWeek1(Integer week1) {
        this.week1 = week1;
    }

    public Integer getWeek2() {
        return week2;
    }

    public void setWeek2(Integer week2) {
        this.week2 = week2;
    }

    public Integer getWeek3() {
        return week3;
    }

    public void setWeek3(Integer week3) {
        this.week3 = week3;
    }

    public Integer getWeek4() {
        return week4;
    }

    public void setWeek4(Integer week4) {
        this.week4 = week4;
    }

    public Integer getWeek5() {
        return week5;
    }

    public void setWeek5(Integer week5) {
        this.week5 = week5;
    }

    public Integer getWeek6() {
        return week6;
    }

    public void setWeek6(Integer week6) {
        this.week6 = week6;
    }

    public Integer getWeek7() {
        return week7;
    }

    public void setWeek7(Integer week7) {
        this.week7 = week7;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appId=").append(appId);
        sb.append(", refWeek=").append(refWeek);
        sb.append(", dataType=").append(dataType);
        sb.append(", week0=").append(week0);
        sb.append(", week1=").append(week1);
        sb.append(", week2=").append(week2);
        sb.append(", week3=").append(week3);
        sb.append(", week4=").append(week4);
        sb.append(", week5=").append(week5);
        sb.append(", week6=").append(week6);
        sb.append(", week7=").append(week7);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}