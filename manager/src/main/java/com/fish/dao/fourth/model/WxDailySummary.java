package com.fish.dao.fourth.model;

import java.util.Date;

public class WxDailySummary {
    private Integer id;

    private String appId;

    private String refDate;

    private Integer visitTotal;

    private Integer sharePv;

    private Integer shareUv;

    private Date insertTime;

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

    public Integer getVisitTotal() {
        return visitTotal;
    }

    public void setVisitTotal(Integer visitTotal) {
        this.visitTotal = visitTotal;
    }

    public Integer getSharePv() {
        return sharePv;
    }

    public void setSharePv(Integer sharePv) {
        this.sharePv = sharePv;
    }

    public Integer getShareUv() {
        return shareUv;
    }

    public void setShareUv(Integer shareUv) {
        this.shareUv = shareUv;
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
        sb.append(", visitTotal=").append(visitTotal);
        sb.append(", sharePv=").append(sharePv);
        sb.append(", shareUv=").append(shareUv);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}