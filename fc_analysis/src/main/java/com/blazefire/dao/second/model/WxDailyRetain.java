package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 小程序日留存
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 15:38
 */
@Data
public class WxDailyRetain {

    private String appId;
    private String refDate;
    private String dataType;
    private int day0 = 0;
    private int day1 = 0;
    private int day2 = 0;
    private int day3 = 0;
    private int day4 = 0;
    private int day5 = 0;
    private int day6 = 0;
    private int day7 = 0;
    private int day14 = 0;
    private int day30 = 0;

    public WxDailyRetain(String appId, String refDate, String dataType) {
        this.appId = appId;
        this.refDate = refDate;
        this.dataType = dataType;
    }

}
