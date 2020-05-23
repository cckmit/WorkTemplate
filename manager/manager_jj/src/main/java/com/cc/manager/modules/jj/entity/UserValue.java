package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-14
 */
@Data
@TableName(schema = "persie", value = "user_value")
public class UserValue implements BaseCrudEntity<UserValue> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId("ddUid")
    private String ddUid;

    /**
     * 用户累计奖金
     */
    @TableField("ddAwardMoney")
    private Integer ddAwardMoney;

    /**
     * 用户累计奖币
     */
    @TableField("ddAwardCoin")
    private Integer ddAwardCoin;

    /**
     * 用户金币数量
     */
    @TableField("ddCoinCount")
    private Integer ddCoinCount;

    /**
     * 用户人民币数量
     */
    @TableField("ddMoney")
    private Integer ddMoney;

    /**
     * 用户累计充值金额
     */
    @TableField("ddTotalPayMoney")
    private Integer ddTotalPayMoney;

    /**
     * 运营权限
     */
    @TableField("ddGameMaster")
    private Boolean ddGameMaster;

    /**
     * 用户更新时间
     */
    @TableField("ddLoginTime")
    private LocalDateTime ddLoginTime;

    @Override
    public String getCacheKey() {
        return this.ddUid;
    }

    @Override
    public String getCacheValue() {
        return this.ddUid + "-" + this.ddMoney;
    }
}
