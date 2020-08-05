package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 小程序数据日趋势
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:36
 */
@Data
public class WxVisitTrend {

    private String appId;
    /**
     * 日期，格式为 yyyymmdd
     */
    private String refDate;
    /**
     * 打开次数
     */
    private int sessionCnt;
    /**
     * 访问次数
     */
    private int visitPv;
    /**
     * 访问人数
     */
    private int visitUv;
    /**
     * 新用户数
     */
    private int visitUvNew;
    /**
     * 人均停留时长 (浮点型，单位：秒)
     */
    private double stayTimeUv;
    /**
     * 次均停留时长 (浮点型，单位：秒)
     */
    private double stayTimeSession;
    /**
     * 平均访问深度 (浮点型)
     */
    private double visitDepth;

    public WxVisitTrend(String appId) {
        this.appId = appId;
    }

}
