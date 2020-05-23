package com.cc.manager.modules.fc.entity;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "persie_value", value = "ad_value_wx_adunit")
public class AdValueWxAdUnit extends BaseStatsEntity<AdValueWxAdUnit> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private String date;

    /**
     * appId
     */
    @TableField("appId")
    private String appId;

    /**
     * 数据来源，区分jj和fc
     */
    @TableField("appSource")
    private String appSource;

    /**
     * 广告位id
     */
    @TableField("adUnitId")
    private String adUnitId;

    /**
     * 广告位名称
     */
    @TableField("adUnitName")
    private String adUnitName;

    /**
     * 拉取量
     */
    @TableField("reqSuccCount")
    private int reqSuccCount;

    /**
     * 曝光量
     */
    @TableField("exposureCount")
    private int exposureCount;

    /**
     * 曝光率
     */
    @TableField("exposureRate")
    private String exposureRate = "0.00%";

    /**
     * 点击量
     */
    @TableField("clickCount")
    private int clickCount;

    /**
     * 点击率
     */
    @TableField("clickRate")
    private String clickRate = "0.00%";

    /**
     * 收入(分)
     */
    private Integer income = 0;

    /**
     * 广告千次曝光收益(分)
     */
    @TableField(exist = false)
    private double ecpm = 0;

    /**
     * 广告位
     */
    @TableField("slotId")
    private String slotId;

    /**
     * 数据插入时间
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;

    /**
     * 展示字段-产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 单次点击收入
     */
    @TableField(exist = false)
    private float clickIncome;

    /*** 插屏总收入*/
    @TableField(exist = false)
    private String screenIncome;

    /**
     * 数据累加
     *
     * @param adValueWxAdUnit 另一条广告数据
     */
    public void merge(AdValueWxAdUnit adValueWxAdUnit) {
        this.reqSuccCount += adValueWxAdUnit.reqSuccCount;
        this.exposureCount += adValueWxAdUnit.exposureCount;
        this.clickCount += adValueWxAdUnit.clickCount;
        this.income += adValueWxAdUnit.income;

    }

    /**
     * 计算点击率和中转点击率
     */
    public void calculateRate() {
        if (reqSuccCount != 0) {
            this.exposureRate = NumberUtil.roundStr(NumberUtil.div(100 * this.exposureCount, this.reqSuccCount), 2) + "%";
        }
        if (exposureCount != 0) {
            this.clickRate = NumberUtil.roundStr(NumberUtil.div(100 * this.clickCount, this.exposureCount), 2) + "%";
        }
        this.ecpm = new BigDecimal((float) this.income * 1000 / this.exposureCount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.clickIncome = (float) this.income / this.clickCount;
    }

}
