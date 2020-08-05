package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 用户访问小程序周留存
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 23:24
 */
@Data
public class WxWeeklyRetain {

    private String appId;
    private String refWeek;
    private String dataType;
    private int week0 = 0;
    private int week1 = 0;
    private int week2 = 0;
    private int week3 = 0;
    private int week4 = 0;
    private int week5 = 0;
    private int week6 = 0;
    private int week7 = 0;

    public WxWeeklyRetain(String appId, String refWeek, String dataType) {
        this.appId = appId;
        this.refWeek = refWeek;
        this.dataType = dataType;
    }

}
