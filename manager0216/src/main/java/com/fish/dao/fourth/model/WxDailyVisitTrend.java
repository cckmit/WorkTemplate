package com.fish.dao.fourth.model;

/**
 * @author Host-0311
 * @date 2020-04-02 13:54:45
 */
public class WxDailyVisitTrend {

    /**
     * ID
     */
    private String id;
    /**
     * appID
     */
    private String appId;

    /**
     * 名称
     */
    private String productName;

    /**
     * 日期
     */
    private String refDate;
    /**
     * 打开次数
     */
    private Integer sessionCnt;
    /**
     * 访问次数
     */
    private Integer visitPv;
    /**
     * 访问人数
     */
    private Integer visitUv;
    /**
     * 新用户数
     */
    private Integer visitUvNew;
    /**
     * 人均停留时长（单位：秒）
     */
    private Double stayTimeUv;
    /**
     * 次均停留时长（单位：秒）
     */
    private Double stayTimeSession;
    /**
     * 平均访问深度
     */
    private Double visitDepth;
    /**
     * 数据插入时间
     */
    private String insertTime;

    /**
     * 查询开始和结束时间
     */
    private String beginTime;
    private String endTime;

    /**
     * 查询类型
     */
    private String queryType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public Integer getSessionCnt() {
        return sessionCnt;
    }

    public void setSessionCnt(Integer sessionCnt) {
        this.sessionCnt = sessionCnt;
    }

    public Integer getVisitPv() {
        return visitPv;
    }

    public void setVisitPv(Integer visitPv) {
        this.visitPv = visitPv;
    }

    public Integer getVisitUv() {
        return visitUv;
    }

    public void setVisitUv(Integer visitUv) {
        this.visitUv = visitUv;
    }

    public Integer getVisitUvNew() {
        return visitUvNew;
    }

    public void setVisitUvNew(Integer visitUvNew) {
        this.visitUvNew = visitUvNew;
    }

    public Double getStayTimeUv() {
        return stayTimeUv;
    }

    public void setStayTimeUv(Double stayTimeUv) {
        this.stayTimeUv = stayTimeUv;
    }

    public Double getStayTimeSession() {
        return stayTimeSession;
    }

    public void setStayTimeSession(Double stayTimeSession) {
        this.stayTimeSession = stayTimeSession;
    }

    public Double getVisitDepth() {
        return visitDepth;
    }

    public void setVisitDepth(Double visitDepth) {
        this.visitDepth = visitDepth;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
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

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

}
