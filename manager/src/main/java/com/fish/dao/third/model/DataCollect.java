package com.fish.dao.third.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DataCollect implements Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date wxDate;
    /*** 产品数量*/
    private Integer productCount;
    /*** 新增数量*/
    private Integer newCount;
    /*** 活跃数量*/
    private Integer activeCount;
    /*** 总收入*/
    private BigDecimal revenueCount;
    /*** 广告总收入*/
    private BigDecimal adRevenueCount;
    /*** 插屏总收入*/
    private BigDecimal screenIncomeCount;
    /*** 充值总收入*/
    private BigDecimal rechargeCount;
    /*** 视频总收入*/
    private BigDecimal videoIncomeCount;

    /*** banner总收入*/
    private BigDecimal bannerIncomeCount;
    /*** 买量支出*/
    private BigDecimal buyPay;
    /*** 分享人数*/
    private Integer shareUserCount;
    /*** 分享次数*/
    private Integer shareCount;
    /*** 分享率*/
    private BigDecimal shareRateCount;

    /**
     * 查询的开始和结束时间
     */
    public String beginTime;
    public String endTime;

    public BigDecimal getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(BigDecimal rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public BigDecimal getScreenIncomeCount() {
        return screenIncomeCount;
    }

    public void setScreenIncomeCount(BigDecimal screenIncomeCount) {
        this.screenIncomeCount = screenIncomeCount;
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

    public Date getWxDate() {
        return wxDate;
    }

    public void setWxDate(Date wxDate) {
        this.wxDate = wxDate;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public BigDecimal getRevenueCount() {
        return revenueCount;
    }

    public void setRevenueCount(BigDecimal revenueCount) {
        this.revenueCount = revenueCount;
    }

    public BigDecimal getAdRevenueCount() {
        return adRevenueCount;
    }

    public void setAdRevenueCount(BigDecimal adRevenueCount) {
        this.adRevenueCount = adRevenueCount;
    }

    public BigDecimal getVideoIncomeCount() {
        return videoIncomeCount;
    }

    public void setVideoIncomeCount(BigDecimal videoIncomeCount) {
        this.videoIncomeCount = videoIncomeCount;
    }

    public BigDecimal getBannerIncomeCount() {
        return bannerIncomeCount;
    }

    public void setBannerIncomeCount(BigDecimal bannerIncomeCount) {
        this.bannerIncomeCount = bannerIncomeCount;
    }

    public BigDecimal getBuyPay() {
        return buyPay;
    }

    public void setBuyPay(BigDecimal buyPay) {
        this.buyPay = buyPay;
    }

    public Integer getShareUserCount() {
        return shareUserCount;
    }

    public void setShareUserCount(Integer shareUserCount) {
        this.shareUserCount = shareUserCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public BigDecimal getShareRateCount() {
        return shareRateCount;
    }

    public void setShareRateCount(BigDecimal shareRateCount) {
        this.shareRateCount = shareRateCount;
    }
}
