package com.cc.manager.modules.tt.entity;

import lombok.Data;

/**
 * @author cf
 * @date 2020-08-07
 */
@Data
public class TransferData {

    /**
     * appId
     */
    private String appId;
    private String dateValue;
    /**
     * 活跃用户数
     */
    private int activeUserNum;
    /**
     * SP曝光
     */
    private int spExposure;
    /**
     * SP点击数
     */
    private int spClicksNum;
    /**
     * SP点击率
     */
    private String spClicksRate;
    /**
     * SP收入
     */
    private String spIncome;

    /**
     * BN曝光
     */
    private int bnExposure;

    /**
     * BN点击数
     */
    private int bnClicksNum;

    /**
     * BN点击率
     */
    private String bnClicksRate;

    /**
     * BN收入
     */
    private String bnIncome;

    /**
     * 活跃用户数
     */
    private String activeUsers;
}
