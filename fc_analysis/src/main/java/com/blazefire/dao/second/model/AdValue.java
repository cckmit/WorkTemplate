package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 广告数据统计
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-09 22:16
 */
@Data
public class AdValue {
    /**
     * 小时值
     */
    private int hourNum;
    /**
     * AppId
     */
    private String appId;
    /**
     * 平台
     */
    private String appPlatform;
    /**
     * 类型
     */
    private int appType;
    /**
     * 版本
     */
    private String version;
    /**
     * 广告位置ID
     */
    private int adPositionId = 0;
    /**
     * 广告位ID
     */
    private int adSpaceId = 0;
    /**
     * 广告内容ID
     */
    private int adContentId = 0;
    /**
     * 广告位置
     */
    private int adShowIndex = 0;
    /**
     * 展示次数
     */
    private int showNum = 0;
    /**
     * 点击次数
     */
    private int clickNum = 0;
    /**
     * 中转App展示次数
     */
    private int promoteShowNum = 0;
    /**
     * 中转App点击次数
     */
    private int promoteClickNum = 0;
    /**
     * 目标App展示次数
     */
    private int targetShowNum = 0;

    public void setTypeNum(String type) {
        switch (type) {
            // 展示
            case "show":
                this.showNum = 1;
                break;
            // 点击
            case "click":
                this.clickNum = 1;
                break;
            // 中转App展示
            case "promoteShow":
                this.promoteShowNum = 1;
                break;
            // 中转App点击
            case "promoteClick":
                this.promoteClickNum = 1;
                break;
            case "target":
                this.targetShowNum = 1;
                break;
            default:
                break;
        }
    }

    public String getKey() {
        return hourNum + "_" + appId + "_" + version + "_" + adPositionId + "_" + adSpaceId + "_" + adContentId + "_" + adShowIndex;
    }

}
