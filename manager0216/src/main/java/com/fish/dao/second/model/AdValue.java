package com.fish.dao.second.model;

/**
 * 广告数据查询
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-10 13:45
 */
public class AdValue {

    private int dateNum;
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

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
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
}
