package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-08
 */
@Data
@TableName(schema = "persie_deamon", value = "round_record")
public class RoundRecord extends BaseStatsEntity<RoundRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 赛场编号
     */
    @TableId("ddCode")
    private Integer ddCode;

    /**
     * 群标签
     */
    @TableField("ddGroup")
    private Boolean ddGroup;

    /**
     * 赛场轮次
     */
    @TableField("ddIndex")
    private Integer ddIndex;

    /**
     * 赛区名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 游戏编号
     */
    @TableField("ddGame")
    private Integer ddGame;

    /**
     * 赛场索引
     */
    @TableField("ddRound")
    private String ddRound;

    /**
     * 赛场开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddStart", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddStart;

    /**
     * 赛场结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddEnd", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddEnd;

    /**
     * 赛场结算时间
     */

    @TableField(value = "ddSubmit", fill = FieldFill.INSERT_UPDATE)
    private Date ddSubmit;

    /**
     * 排行数
     */
    @TableField("ddResult")
    private Integer ddResult;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddTime;


    /**
     * 展示数据--小游戏参数-游戏名称
     */
    @TableField(exist = false)
    private String gamesName;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String appName;
    /**
     * 产品appId
     */
    @TableField(exist = false)
    private String appId;

    /**
     * 赛制名称
     */
    @TableField(exist = false)
    private String roundName;
    /**
     * 赛制时长
     */
    @TableField(exist = false)
    private String roundLength;


}
