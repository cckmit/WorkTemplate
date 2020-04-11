package com.fish.dao.fourth.model;

/**
 *
 */
public class AdValueWxAdUnit {
    /**
     * ID
     */
    private String id;
    /**
     * 时间
     */
    private String date;
    /**
     * appID
     */
    private String appId;
    /**
     *
     */
    private String adUnitId;
    /**
     *
     */
    private String adUnitName;
    /**
     * 点击量
     */
    private Integer clickCount;
    /**
     * 点击率
     */
    private Double clickRate;
    /**
     * 广告千次曝光收益(分)
     */
    private Double ecpm;
    /**
     * 曝光量
     */
    private Integer exposureCount;
    /**
     * 曝光率
     */
    private Double exposureRate;
    /**
     * 收入
     */
    private Integer income;
    /**
     * 总拉取量
     */
    private Integer reqSuccCount;

    /**
     * 单次点击收入
     */
    private Double clickIncome;
    /**
     * 广告位
     */
    private String slotId;

    /**
     * 查询的开始和结束时间
     */
    private String beginTime;
    private String endTime;



    /**
     * 分组方式:
     *    0:时间
     *    1:产品
     *    2:广告类型
     *    3:广告位
     *    4:产品+广告类型
     *    5:产品+广告位
     */
    private String groupByType;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 查询详情标志0:未查- 1:查询了详情
     */
    private String queryDetail;

    /*** 插屏总收入*/
    private String screenIncome;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public String getAdUnitName() {
        return adUnitName;
    }

    public void setAdUnitName(String adUnitName) {
        this.adUnitName = adUnitName;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Double getClickRate() {
        return clickRate;
    }

    public void setClickRate(Double clickRate) {
        this.clickRate = clickRate;
    }

    public Double getEcpm() {
        return ecpm;
    }

    public void setEcpm(Double ecpm) {
        this.ecpm = ecpm;
    }

    public Integer getExposureCount() {
        return exposureCount;
    }

    public void setExposureCount(Integer exposureCount) {
        this.exposureCount = exposureCount;
    }

    public Double getExposureRate() {
        return exposureRate;
    }

    public void setExposureRate(Double exposureRate) {
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

    public Double getClickIncome() {
        return clickIncome;
    }

    public void setClickIncome(Double clickIncome) {
        this.clickIncome = clickIncome;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
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

    /*public String getGroupTimeType() {
        return groupTimeType;
    }

    public void setGroupTimeType(String groupTimeType) {
        this.groupTimeType = groupTimeType;
    }*/

    public String getGroupByType() {
        return groupByType;
    }

    public void setGroupByType(String groupByType) {
        this.groupByType = groupByType;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQueryDetail() {
        return queryDetail;
    }

    public void setQueryDetail(String queryDetail) {
        this.queryDetail = queryDetail;
    }

    public String getScreenIncome() {
        return screenIncome;
    }

    public void setScreenIncome(String screenIncome) {
        this.screenIncome = screenIncome;
    }
}
