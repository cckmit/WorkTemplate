package com.fish.dao.fourth.model;

import java.util.Date;

public class WxMonthlyRetain {
    private Integer id;

    private String appId;

    private String refMonth;

    private String dataType;

    private Integer month0;

    private Integer month1;

    private Integer month2;

    private Integer month3;

    private Integer month4;

    private Integer month5;

    private Integer month6;

    private Integer month7;

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

    public String getRefMonth() {
        return refMonth;
    }

    public void setRefMonth(String refMonth) {
        this.refMonth = refMonth == null ? null : refMonth.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public Integer getMonth0() {
        return month0;
    }

    public void setMonth0(Integer month0) {
        this.month0 = month0;
    }

    public Integer getMonth1() {
        return month1;
    }

    public void setMonth1(Integer month1) {
        this.month1 = month1;
    }

    public Integer getMonth2() {
        return month2;
    }

    public void setMonth2(Integer month2) {
        this.month2 = month2;
    }

    public Integer getMonth3() {
        return month3;
    }

    public void setMonth3(Integer month3) {
        this.month3 = month3;
    }

    public Integer getMonth4() {
        return month4;
    }

    public void setMonth4(Integer month4) {
        this.month4 = month4;
    }

    public Integer getMonth5() {
        return month5;
    }

    public void setMonth5(Integer month5) {
        this.month5 = month5;
    }

    public Integer getMonth6() {
        return month6;
    }

    public void setMonth6(Integer month6) {
        this.month6 = month6;
    }

    public Integer getMonth7() {
        return month7;
    }

    public void setMonth7(Integer month7) {
        this.month7 = month7;
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
        sb.append(", refMonth=").append(refMonth);
        sb.append(", dataType=").append(dataType);
        sb.append(", month0=").append(month0);
        sb.append(", month1=").append(month1);
        sb.append(", month2=").append(month2);
        sb.append(", month3=").append(month3);
        sb.append(", month4=").append(month4);
        sb.append(", month5=").append(month5);
        sb.append(", month6=").append(month6);
        sb.append(", month7=").append(month7);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}