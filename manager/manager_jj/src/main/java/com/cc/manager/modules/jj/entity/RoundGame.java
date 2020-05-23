package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@Data
@TableName(schema = "persie_deamon", value = "round_game")
public class RoundGame implements BaseCrudEntity<RoundGame> {

    private static final long serialVersionUID=1L;

    /**
     * 游戏赛场编号
     */
    @TableId(value = "ddCode", type = IdType.AUTO)
    private Integer ddCode;

    /**
     * 游戏赛制名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 状态
     */
    @TableField("ddState")
    private Boolean ddState;

    /**
     * 游戏编号
     */
    @TableField("ddGame")
    private Integer ddGame;

    /**
     * 赛制编号
     */
    @TableField("ddRound")
    private String ddRound;

    /**
     * 开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddStart")
    private LocalDateTime ddStart;

    /**
     * 截止时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddEnd")
    private LocalDateTime ddEnd;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "times", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime times;

    /**
     * 展示数据-游戏名称
     */
    @TableField(exist = false)
    private  String gameName;

    /**
     * 展示数据-赛制名称
     */
    @TableField(exist = false)
    private  String roundName;

    /**
     * 展示数据-赛制时长
     */
    @TableField(exist = false)
    private  String roundLength;

    /**
     * 展示数据-奖励说明
     */
    @TableField(exist = false)
    private  String ddReward;

    /**
     * 展示数据-资源图
     */
    @TableField(exist = false)
    private  String jumpDirect;
    @Override
    public String getCacheKey() {
        return this.ddCode.toString();
    }

    @Override
    public String getCacheValue() {
        return this.ddCode + "-" + this.ddName;
    }
}
