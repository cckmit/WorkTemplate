package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie", value = "goods_value_ext")
public class GoodsValueExt implements BaseCrudEntity<GoodsValueExt> {

    private static final long serialVersionUID = 1L;

    /**
     * 全局编号
     */
    @TableId(value = "ddId", type = IdType.AUTO)
    private Integer id;

    /**
     * 全局状态
     */
    @TableField("ddState")
    private Boolean ddState;

    /**
     * 匹配规则,位运算:0x01为小游戏,0x02为小程序,0x04为公众号
     */
    @TableField("ddMatch")
    private Integer ddMatch;

    /**
     * 支持最低版本
     */
    @TableField("ddMinVer")
    private String ddMinVer;

    /**
     * 是否支持IOS
     */
    @TableField("ddIOS")
    private Boolean ddIOS;

    /**
     * 全局名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 全局描述
     */
    @TableField("ddDesc")
    private String ddDesc;

    /**
     * 消耗类型
     */
    @TableField("ddCostType")
    private String ddCostType;

    /**
     * 消耗价格
     */
    @TableField("ddPrice")
    private BigDecimal ddPrice;

    /**
     * 全局类型
     */
    @TableField("ddGoodsType")
    private String ddGoodsType;

    /**
     * 全局数值
     */
    @TableField("ddValue")
    private Integer ddValue;

    /**
     * 首充翻倍
     */
    @TableField("ddFirst")
    private Boolean ddFirst;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "insertTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime insertTime;

    /**
     * 展示数据-金币数量
     */
    @TableField(exist = false)
    private String coinNumber;

    /**
     * 展示数据-头像框号码
     */
    @TableField(exist = false)
    private String headNumber;
    /**
     * 展示数据-提现金额
     */
    @TableField(exist = false)
    private String cashNumber;
    /**
     * 展示数据-计费点描述
     */
    @TableField(exist = false)
    private String costDesc;
    /**
     * 展示数据-提现描述
     */
    @TableField(exist = false)
    private String gainDesc;

    @Override
    public String getCacheKey() {
        return this.id.toString();
    }

    @Override
    public String getCacheValue() {
        return this.id + "-" + this.ddDesc;
    }

}
