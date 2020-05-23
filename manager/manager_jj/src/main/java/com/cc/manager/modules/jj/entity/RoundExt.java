package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-11
 */
@Data
@TableName(schema = "persie_deamon", value = "round_ext")
public class RoundExt implements BaseCrudEntity<RoundExt> {

    private static final long serialVersionUID = 1L;

    /**
     * 赛场编号
     */
    @TableId("ddCode")
    private String id;

    /**
     * 赛制类型，0-小程序，1-小游戏
     */
    @TableField("ddType")
    private Integer ddType;

    /**
     * 群标志变更1-小程序，0-小游戏
     */
    @TableField("ddGroup")
    private Integer ddGroup;

    /**
     * 赛场名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 优先级
     */
    @TableField("ddPriority")
    private Integer ddPriority;

    /**
     * 赛场状态
     */
    @TableField("ddState")
    private Boolean ddState;

    /**
     * 赛场一轮时长(单位:s)
     */
    @TableField("ddTime")
    private Long ddTime;

    /**
     * 赛场奖励
     */
    @TableField("ddReward")
    private String ddReward;

    /**
     * 赛场描述
     */
    private String tip;

    /**
     * 下发赛场描述
     */
    @TableField("ddDesc")
    private String ddDesc;

    /**
     * 赛场插入时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "insertTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime insertTime;
    /**
     * 展示字段-输入参数赛制时长
     */
    @TableField(exist = false)
    private String roundLength;

    @Override
    public String getCacheKey() {
        return this.id;
    }

    @Override
    public String getCacheValue() {
        return this.id + "-" + this.ddName;
    }

}
