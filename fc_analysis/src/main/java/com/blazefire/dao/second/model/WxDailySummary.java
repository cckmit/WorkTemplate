package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 小程序数据概况
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:24
 */
@Data
public class WxDailySummary {

    private String appId;
    /**
     * 日期，格式为 yyyymmdd
     */
    private String refDate;
    /**
     * 累计用户数
     */
    private int visitTotal = 0;
    /**
     * 转发次数
     */
    private int sharePv = 0;
    /**
     * 转发人数
     */
    private int shareUv = 0;

}
