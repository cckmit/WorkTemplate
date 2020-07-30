package com.cc.manager.modules.tt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-07-27
 */
@Data
@TableName(schema = "qq_data_analysis", value = "daily_ad_value")
public class QqDailyAdValue implements BaseCrudEntity<QqDailyAdValue> {

    private static final long serialVersionUID = 1L;

    /**
     * appId
     */
    @TableId("appId")
    private String wxAppId;

    /**
     * 时间
     */
    @JSONField(format = "yyyy-MM-dd")
    @TableField("dateValue")
    private LocalDate wxDate;

    /**
     * App平台
     */
    @TableField("appType")
    private String wxAppType;

    /**
     * 活跃人数
     */
    @TableField(exist = false)
    private int wxActive;

    /**
     * 视频曝光
     */
    @TableField("videoShow")
    private int wxVideoShow = 0;

    /**
     * 视频点击次数
     */
    @TableField("videoClickCount")
    private int wxVideoClickCount = 0;

    /**
     * 视频点击率
     */
    @TableField("videoClickRate")
    private BigDecimal wxVideoClickrate = new BigDecimal(0);

    /**
     * 视频收入
     */
    @TableField("videoIncome")
    private BigDecimal wxVideoIncome = new BigDecimal(0);

    /**
     * banner曝光
     */
    @TableField("bannerShow")
    private int wxBannerShow = 0;

    /**
     * banner点击次数
     */
    @TableField("bannerClickCount")
    private int wxBannerClickCount = 0;

    /**
     * banner点击率
     */
    @TableField("bannerClickRate")
    private BigDecimal wxBannerClickrate = new BigDecimal(0);

    /**
     * banner收入
     */
    @TableField("bannerIncome")
    private BigDecimal wxBannerIncome = new BigDecimal(0);

    /**
     * 插屏曝光
     */
    @TableField("intShow")
    private int wxIntShow = 0;

    /**
     * banner点击次数
     */
    @TableField("intClickCount")
    private int wxIntClickCount = 0;

    /**
     * 插屏点击率
     */
    @TableField("intClickRate")
    private BigDecimal wxIntClickrate = new BigDecimal(0);

    /**
     * 插屏收入
     */
    @TableField("intIncome")
    private BigDecimal wxIntIncome = new BigDecimal(0);

    @TableField("insertTime")
    private LocalDateTime insertTime;

    /**
     * 产品类型
     */
    @TableField(exist = false)
    private Integer programType;
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    @TableField(exist = false)
    private String ddAppPlatform;

    /**
     * 广告收入
     */
    @TableField(exist = false)
    private BigDecimal adRevenue = new BigDecimal(0);

    /**
     * videoECPM
     */
    @TableField(exist = false)
    private BigDecimal videoECPM = new BigDecimal(0);
    /**
     * bannerECPM
     */
    @TableField(exist = false)
    private BigDecimal bannerECPM = new BigDecimal(0);

    @TableField(exist = false)
    private BigDecimal intECPM = new BigDecimal(0);

    /**
     * 总收入
     */
    @TableField(exist = false)
    private BigDecimal revenueCount;

}
