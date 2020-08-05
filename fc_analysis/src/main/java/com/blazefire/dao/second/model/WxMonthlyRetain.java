package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 小程序月留存
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 15:38
 */
@Data
public class WxMonthlyRetain {

    private String appId;
    private String refMonth;
    private String dataType;
    private int month0 = 0;
    private int month1 = 0;
    private int month2 = 0;
    private int month3 = 0;
    private int month4 = 0;
    private int month5 = 0;
    private int month6 = 0;
    private int month7 = 0;

    public WxMonthlyRetain(String appId, String refMonth, String dataType) {
        this.appId = appId;
        this.refMonth = refMonth;
        this.dataType = dataType;
    }

}
