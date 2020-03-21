package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ConfigSubscribe {
    private String ddAppId;

    private String ddId;

    private Integer ddNo;

    private String ddType;

    private String ddTitle;

    private String ddExplain;

    private Integer ddStatus;

    private String ddRule;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date times;

    public String getDdAppId() {
        return ddAppId;
    }

    public void setDdAppId(String ddAppId) {
        this.ddAppId = ddAppId == null ? null : ddAppId.trim();
    }

    public String getDdId() {
        return ddId;
    }

    public void setDdId(String ddId) {
        this.ddId = ddId == null ? null : ddId.trim();
    }

    public Integer getDdNo() {
        return ddNo;
    }

    public void setDdNo(Integer ddNo) {
        this.ddNo = ddNo;
    }

    public String getDdType() {
        return ddType;
    }

    public void setDdType(String ddType) {
        this.ddType = ddType == null ? null : ddType.trim();
    }

    public String getDdTitle() {
        return ddTitle;
    }

    public void setDdTitle(String ddTitle) {
        this.ddTitle = ddTitle == null ? null : ddTitle.trim();
    }

    public String getDdExplain() {
        return ddExplain;
    }

    public void setDdExplain(String ddExplain) {
        this.ddExplain = ddExplain == null ? null : ddExplain.trim();
    }

    public Integer getDdStatus() {
        return ddStatus;
    }

    public void setDdStatus(Integer ddStatus) {
        this.ddStatus = ddStatus;
    }

    public String getDdRule() {
        return ddRule;
    }

    public void setDdRule(String ddRule) {
        this.ddRule = ddRule == null ? null : ddRule.trim();
    }

    public Date getTimes() {
        return times;
    }

    public void setTimes(Date times) {
        this.times = times;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddAppId=").append(ddAppId);
        sb.append(", ddId=").append(ddId);
        sb.append(", ddNo=").append(ddNo);
        sb.append(", ddType=").append(ddType);
        sb.append(", ddTitle=").append(ddTitle);
        sb.append(", ddExplain=").append(ddExplain);
        sb.append(", ddStatus=").append(ddStatus);
        sb.append(", ddRule=").append(ddRule);
        sb.append(", times=").append(times);
        sb.append("]");
        return sb.toString();
    }
}