package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsValue {

    private Integer ddId;

    private Boolean ddState;

    private Integer ddMatch;

    private String ddMinVer;

    private Boolean ddIOS;

    private String ddName;

    private String ddDesc;

    private String ddCostType;

    private BigDecimal ddPrice;

    private String ddGoodsType;

    private Integer ddValue;

    private Boolean ddFirst;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;

    /**
     * 展示数据-金币数量
     */
    private String coinNumber;

    /**
     * 展示数据-头像框号码
     */
    private String headNumber;
    /**
     * 展示数据-提现金额
     */
    private String cashNumber;
    /**
     * 展示数据-计费点描述
     */
    private String costDesc;
    /**
     * 展示数据-提现描述
     */
    private String gainDesc;

    public Integer getDdId() {
        return ddId;
    }

    public void setDdId(Integer ddId) {
        this.ddId = ddId;
    }

    public Boolean getDdState() {
        return ddState;
    }

    public void setDdState(Boolean ddState) {
        this.ddState = ddState;
    }

    public Integer getDdMatch() {
        return ddMatch;
    }

    public void setDdMatch(Integer ddMatch) {
        this.ddMatch = ddMatch;
    }

    public String getDdMinVer() {
        return ddMinVer;
    }

    public void setDdMinVer(String ddMinVer) {
        this.ddMinVer = ddMinVer == null ? null : ddMinVer.trim();
    }

    public Boolean getDdIOS() {
        return ddIOS;
    }

    public void setDdIOS(Boolean ddIOS) {
        this.ddIOS = ddIOS;
    }

    public String getDdName() {
        return ddName;
    }

    public void setDdName(String ddName) {
        this.ddName = ddName == null ? null : ddName.trim();
    }

    public String getDdDesc() {
        return ddDesc;
    }

    public void setDdDesc(String ddDesc) {
        this.ddDesc = ddDesc == null ? null : ddDesc.trim();
    }

    public String getDdCostType() {
        return ddCostType;
    }

    public void setDdCostType(String ddCostType) {
        this.ddCostType = ddCostType == null ? null : ddCostType.trim();
    }

    public BigDecimal getDdPrice() {
        return ddPrice;
    }

    public void setDdPrice(BigDecimal ddPrice) {
        this.ddPrice = ddPrice;
    }

    public String getDdGoodsType() {
        return ddGoodsType;
    }

    public void setDdGoodsType(String ddGoodsType) {
        this.ddGoodsType = ddGoodsType == null ? null : ddGoodsType.trim();
    }

    public Integer getDdValue() {
        return ddValue;
    }

    public void setDdValue(Integer ddValue) {
        this.ddValue = ddValue;
    }

    public Boolean getDdFirst() {
        return ddFirst;
    }

    public void setDdFirst(Boolean ddFirst) {
        this.ddFirst = ddFirst;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getCoinNumber() {
        return coinNumber;
    }

    public void setCoinNumber(String coinNumber) {
        this.coinNumber = coinNumber;
    }

    public String getHeadNumber() {
        return headNumber;
    }

    public void setHeadNumber(String headNumber) {
        this.headNumber = headNumber;
    }

    public String getCashNumber() {
        return cashNumber;
    }

    public void setCashNumber(String cashNumber) {
        this.cashNumber = cashNumber;
    }

    public String getCostDesc() {
        return costDesc;
    }

    public void setCostDesc(String costDesc) {
        this.costDesc = costDesc;
    }

    public String getGainDesc() {
        return gainDesc;
    }

    public void setGainDesc(String gainDesc) {
        this.gainDesc = gainDesc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddId=").append(ddId);
        sb.append(", ddState=").append(ddState);
        sb.append(", ddMatch=").append(ddMatch);
        sb.append(", ddMinVer=").append(ddMinVer);
        sb.append(", ddName=").append(ddName);
        sb.append(", ddDesc=").append(ddDesc);
        sb.append(", ddCostType=").append(ddCostType);
        sb.append(", ddPrice=").append(ddPrice);
        sb.append(", ddGoodsType=").append(ddGoodsType);
        sb.append(", ddValue=").append(ddValue);
        sb.append(", ddFirst=").append(ddFirst);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}