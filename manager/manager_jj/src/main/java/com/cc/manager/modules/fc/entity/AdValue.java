package com.cc.manager.modules.fc.entity;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告数据统计模块
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-01 18:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "persie_value", value = "ad_value")
public class AdValue extends BaseStatsEntity<AdValue> {
    /**
     * 时间值，按日为yyyy-MM-dd格式，按小时为yyyy-MM-dd HH格式
     */
    private String timeValue;
    /**
     * 产品AppId
     */
    private String productAppId;
    /**
     * 产品App名称
     */
    private String productAppName;
    /**
     * 广告位置ID
     */
    private int adPositionId;
    /**
     * 广告位置名称
     */
    private String adPositionName;
    /**
     * 广告位ID
     */
    private int adSpaceId;
    /**
     * 广告位名称
     */
    private String adSpaceName;
    /**
     * 广告内容ID
     */
    private int adContentId;
    /**
     * 广告内容名称
     */
    private String adContentName;
    /**
     * 推广appId
     */
    private String targetAppId;
    /**
     * 推广app名称
     */
    private String targetAppName;
    /**
     * 展示次数
     */
    private int showNum;
    /**
     * 点击次数
     */
    private int clickNum;
    /**
     * 中转App展示次数
     */
    private int promoteShowNum;
    /**
     * 中转App点击次数
     */
    private int promoteClickNum;
    /**
     * 目标App展示次数
     */
    private int targetShowNum;
    /**
     * 点击率
     */
    private String clickRate = "0.00%";
    /**
     * 中转点击率
     */
    private String promoteClickRate = "0.00%";

    /**
     * 数据累加
     *
     * @param adValue 另一条广告数据
     */
    public void merge(AdValue adValue) {
        this.showNum += adValue.showNum;
        this.clickNum += adValue.clickNum;
        this.promoteShowNum += adValue.promoteShowNum;
        this.promoteClickNum += adValue.promoteClickNum;
        this.targetShowNum += adValue.targetShowNum;
    }

    /**
     * 计算点击率和中转点击率
     */
    public void calculateRate() {
        if (showNum != 0) {
            this.clickRate = NumberUtil.roundStr(NumberUtil.div(100 * this.clickNum, this.showNum), 2) + "%";
        }
        if (promoteShowNum != 0) {
            this.promoteClickRate = NumberUtil.roundStr(NumberUtil.div(100 * this.promoteClickNum, this.promoteShowNum), 2) + "%";
        }
    }

}
