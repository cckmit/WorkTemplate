package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author cf
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Recharge implements BaseCrudEntity<Recharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId("ddId")
    private String ddId;

    /**
     * 用户编号
     */
    @TableField("ddUid")
    private String ddUid;

    /**
     * 对应平台appId
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 提现人民币
     */
    @TableField("ddRmb")
    private BigDecimal ddRmb;

    /**
     * 提现描述
     */
    @TableField("ddTip")
    private String ddTip;

    /**
     * 提现状态 0申请中,1未绑定,2：  已提现操作，未到账，驳回详情，放置表项ddTip,-1申请驳回,200提现成功
     */
    @TableField("ddStatus")
    private Integer ddStatus;

    /**
     * 完成时间
     */
    @TableField("ddTrans")
    private LocalDateTime ddTrans;

    /**
     * 提现AppId
     */
    @TableField("ddRechargeAppId")
    private String ddRechargeAppId;

    /**
     * 提现OpenId
     */
    @TableField("ddRechargeOpenId")
    private String ddRechargeOpenId;

    /**
     * 操作时间
     */
    @TableField("ddTimes")
    private LocalDateTime ddTimes;


}
