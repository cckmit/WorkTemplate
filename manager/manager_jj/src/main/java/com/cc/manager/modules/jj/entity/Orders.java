package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-13
 */
@Data
@TableName(schema = "persie", value = "orders")
public class Orders extends BaseStatsEntity<Orders> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId("ddId")
    private String id;

    /**
     * 支付用户编号
     */
    @TableField("ddUid")
    private String ddUid;

    /**
     * 订单商品id
     */
    @TableField("ddGId")
    private Integer ddGId;

    /**
     * 支付类型
     */
    @TableField("ddType")
    private String ddType;

    /**
     * 商户平台交易账号
     */
    @TableField("ddAccount")
    private String ddAccount;

    /**
     * 商户平台订单号
     */
    @TableField("ddOrder")
    private String ddOrder;

    /**
     * 商户平台失败原因
     */
    @TableField("ddError")
    private String ddError;

    /**
     * 订单金额
     */
    @TableField("ddPrice")
    private BigDecimal ddPrice;

    /**
     * 订单状态
     */
    @TableField("ddState")
    private Integer ddState;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddTime;

    /**
     * 完成订单
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddTrans", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddTrans;

    /**
     * openid
     */
    @TableField("ddOId")
    private String ddOId;

    /**
     * appid
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 产品类型
     */
    @TableField(exist = false)
    private Integer productType;

    @TableField(exist = false)
    private String originName;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String goodsName;
    /**
     * 描述
     */
    @TableField(exist = false)
    private String ddDesc;
    /**
     * 支付金额
     */
    @TableField(exist = false)
    private Double payMoney;
    /**
     * 支付用户数
     */
    @TableField(exist = false)
    private Integer payUsers;
    @TableField(exist = false)
    private String payUp;

    @TableField(exist = false)
    private boolean haveDetail = false;
}
