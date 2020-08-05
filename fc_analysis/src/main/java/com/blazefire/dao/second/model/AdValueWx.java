package com.blazefire.dao.second.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Wx广告数据，具体数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-29 22:17
 */
@Data
public class AdValueWx {

    private String date;
    private String appId;
    private String appSource;
    private int reqSuccCount;
    private int exposureCount;
    private BigDecimal exposureRate;
    private int clickCount;
    private BigDecimal clickRate;
    private int income;
    private BigDecimal ecpm;
    private String slotId;

    /* 以下两个参数是给小游戏广告细分数据使用的 */
    /**
     * 广告ID
     */
    private String adUnitId;
    /**
     * 广告位名称
     */
    private String adUnitName;

}
