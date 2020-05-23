package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "persie_deamon", value = "round_receive")
public class RoundReceive implements BaseCrudEntity<RoundReceive> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    @TableField("ddUid")
    private String ddUid;

    /**
     * 赛场编号
     */
    @TableField("ddMCode")
    private Integer ddMCode;

    /**
     * 群标志
     */
    @TableField("ddGroup")
    private Boolean ddGroup;

    /**
     * 赛区轮次
     */
    @TableField("ddMIndex")
    private Integer ddMIndex;

    /**
     * 游戏编号
     */
    @TableField("ddGCode")
    private Integer ddGCode;

    /**
     * 赛区开始时间
     */
    @TableField("ddMStart")
    private LocalDateTime ddMStart;

    /**
     * 赛区结束时间
     */
    @TableField("ddMEnd")
    private LocalDateTime ddMEnd;

    /**
     * 得分
     */
    @TableField("ddMark")
    private Long ddMark;

    /**
     * 排名
     */
    @TableField("ddRanking")
    private Integer ddRanking;

    /**
     * 奖励类型
     */
    @TableField("ddType")
    private String ddType;

    /**
     * 奖励数量
     */
    @TableField("ddTotal")
    private Integer ddTotal;

    /**
     * 奖励时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddTime;
    /**
     * 展示字段-用户名称
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 展示字段-游戏名称
     */
    @TableField(exist = false)
    private String gameName;
    /**
     * 展示字段-比赛名称
     */
    @TableField(exist = false)
    private String roundName;
    @TableField(exist = false)
    /**
     * 展示字段-赛制编号
     */
    private String roundCode;
    /**
     * 展示字段-赛制时长
     */
    @TableField(exist = false)
    private String roundTime;

    @TableField(exist = false)
    private boolean haveDetail = true;
}
