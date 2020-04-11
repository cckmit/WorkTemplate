package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class BuyPay {
    private Long id;

    private String buyDate;

    private String buyAppId;

    private String buyProductName;

    private BigDecimal buyCost;

    private Integer buyClickNumber;

    private BigDecimal buyClickPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;

    /**
     * 开始-结束的查询时段
     */
    private String beginTime;
    private String endTime;

    /**
     * 类型0:小游戏  1:小程序
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate == null ? null : buyDate.trim();
    }

    public String getBuyAppId() {
        return buyAppId;
    }

    public void setBuyAppId(String buyAppId) {
        this.buyAppId = buyAppId == null ? null : buyAppId.trim();
    }

    public String getBuyProductName() {
        return buyProductName;
    }

    public void setBuyProductName(String buyProductName) {
        this.buyProductName = buyProductName == null ? null : buyProductName.trim();
    }

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public Integer getBuyClickNumber() {
        return buyClickNumber;
    }

    public void setBuyClickNumber(Integer buyClickNumber) {
        this.buyClickNumber = buyClickNumber;
    }

    public BigDecimal getBuyClickPrice() {
        return buyClickPrice;
    }

    public void setBuyClickPrice(BigDecimal buyClickPrice) {
        this.buyClickPrice = buyClickPrice;
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
        sb.append(", buyDate=").append(buyDate);
        sb.append(", buyAppId=").append(buyAppId);
        sb.append(", buyProductName=").append(buyProductName);
        sb.append(", buyCost=").append(buyCost);
        sb.append(", buyClickNumber=").append(buyClickNumber);
        sb.append(", buyClickPrice=").append(buyClickPrice);
        sb.append(", insertTime=").append(insertTime);
        sb.append("]");
        return sb.toString();
    }
}