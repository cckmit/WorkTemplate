package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 广告按用户按日去重统计数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-18 11:25
 */
@Data
public class AdValueUser {
    /**
     * 小时值
     */
    private int hourNum;
    /**
     * AppId
     */
    private String appId;
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

    public String getKey() {
        return hourNum + "_" + appId + "_" + version + "_" + adPositionId + "_" + adSpaceId + "_" + adContentId + "_" + adShowIndex;
    }
}
