package com.fish.dao.second.model;

import java.util.Date;

public class ConfigProgram {

    private String ddAppId;

    private String ddMinVer;

    private Integer ddCode;

    private String ddUrl;

    private Date times;

    /**
     * 展示数据-产品名称
     */
    private String productName;
    /**
     * 展示数据-产品类型
     */
    private Integer programType;
    /**
     * 展示数据-合集名称
     */
    private String Codename;

    public String getDdAppId() {
        return ddAppId;
    }

    public void setDdAppId(String ddAppId) {
        this.ddAppId = ddAppId;
    }

    public String getDdMinVer() {
        return ddMinVer;
    }

    public void setDdMinVer(String ddMinVer) {
        this.ddMinVer = ddMinVer;
    }

    public Integer getDdCode() {
        return ddCode;
    }

    public void setDdCode(Integer ddCode) {
        this.ddCode = ddCode;
    }

    public String getDdUrl() {
        return ddUrl;
    }

    public void setDdUrl(String ddUrl) {
        this.ddUrl = ddUrl == null ? null : ddUrl.trim();
    }

    public Date getTimes() {
        return times;
    }

    public void setTimes(Date times) {
        this.times = times;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProgramType() {
        return programType;
    }

    public void setProgramType(Integer programType) {
        this.programType = programType;
    }

    public String getCodename() {
        return Codename;
    }

    public void setCodename(String codename) {
        Codename = codename;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddCode=").append(ddCode);
        sb.append(", ddUrl=").append(ddUrl);
        sb.append(", times=").append(times);
        sb.append("]");
        return sb.toString();
    }
}