package com.fish.dao.fourth.model;

import java.util.List;

/**
 * 广告数据查询
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-10 13:45
 */
public class AdValue {

    private int hourNum;
    private String appId;
    private String version;
    private int adPositionId;
    private int adSpaceId;
    private int adContentId;
    private int adShowIndex;
    private int showNum;
    private int clickNum;
    private int promoteShowNum;
    private int promoteClickNum;
    private int targetShowNum;

    // 以下是展示参数
    /**
     * 广告位置
     */
    private String positionName;
    /**
     * 广告位
     */
    private String spaceName;
    /**
     * 广告内容
     */
    private String contentName;
    /**
     * app名称
     */
    private String appName;
    /**
     * app平台类型
     */
    private String appPlatform;

    /**
     * 分组方式:
     * adContent:推广名称
     * asPosition:广告位置
     * productName:产品名称
     * asSpace:广告位
     * all:全部明细
     */
    private String groupByType;

    /**
     * 查询详情标志0:未查- 1:查询了详情
     */
    private String queryDetail;
    /**
     * 推广AppId
     */
    private String ddTargetAppId;
    /**
     * 推广App名称
     */
    private String ddTargetAppName;

    private String contentIds;

    /**
     * 展示字段 广告类型
     */
    private String adTypeName;

    public int getHourNum() {
        return hourNum;
    }

    public void setHourNum(int hourNum) {
        this.hourNum = hourNum;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getAdPositionId() {
        return adPositionId;
    }

    public void setAdPositionId(int adPositionId) {
        this.adPositionId = adPositionId;
    }

    public int getAdSpaceId() {
        return adSpaceId;
    }

    public void setAdSpaceId(int adSpaceId) {
        this.adSpaceId = adSpaceId;
    }

    public int getAdContentId() {
        return adContentId;
    }

    public void setAdContentId(int adContentId) {
        this.adContentId = adContentId;
    }

    public int getAdShowIndex() {
        return adShowIndex;
    }

    public void setAdShowIndex(int adShowIndex) {
        this.adShowIndex = adShowIndex;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public int getPromoteShowNum() {
        return promoteShowNum;
    }

    public void setPromoteShowNum(int promoteShowNum) {
        this.promoteShowNum = promoteShowNum;
    }

    public int getPromoteClickNum() {
        return promoteClickNum;
    }

    public void setPromoteClickNum(int promoteClickNum) {
        this.promoteClickNum = promoteClickNum;
    }

    public int getTargetShowNum() {
        return targetShowNum;
    }

    public void setTargetShowNum(int targetShowNum) {
        this.targetShowNum = targetShowNum;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(String appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getGroupByType() {
        return groupByType;
    }

    public void setGroupByType(String groupByType) {
        this.groupByType = groupByType;
    }

    public String getQueryDetail() {
        return queryDetail;
    }

    public void setQueryDetail(String queryDetail) {
        this.queryDetail = queryDetail;
    }

    public String getDdTargetAppId() {
        return ddTargetAppId;
    }

    public void setDdTargetAppId(String ddTargetAppId) {
        this.ddTargetAppId = ddTargetAppId;
    }

    public String getDdTargetAppName() {
        return ddTargetAppName;
    }

    public void setDdTargetAppName(String ddTargetAppName) {
        this.ddTargetAppName = ddTargetAppName;
    }

    public String getContentIds() {
        return contentIds;
    }

    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }


    public String getAdTypeName() {
        return adTypeName;
    }

    public void setAdTypeName(String adTypeName) {
        this.adTypeName = adTypeName;
    }
}
