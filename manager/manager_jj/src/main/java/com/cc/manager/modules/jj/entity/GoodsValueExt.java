package com.cc.manager.modules.jj.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    private static final long serialVersionUID=1L;

    /**
     * 全局编号
     */
    @TableId(value = "ddId", type = IdType.AUTO)
    private Integer ddId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("insertTime")
    private LocalDateTime insertTime;

    /**
     * 展示数据-金币数量
     */
    @TableField(exist = false)
    private  String coinNumber;

    /**
     * 展示数据-头像框号码
     */
    @TableField(exist = false)
    private  String headNumber;
    /**
     * 展示数据-提现金额
     */
    @TableField(exist = false)
    private  String cashNumber;
    /**
     * 展示数据-计费点描述
     */
    @TableField(exist = false)
    private  String costDesc;
    /**
     * 展示数据-提现描述
     */
    @TableField(exist = false)
    private  String gainDesc;

    @Override
    public String getCacheKey() {
        return this.ddId.toString();
    }

    @Override
    public String getCacheValue() {
        return this.ddId + "-" + this.ddDesc;
    }
}
