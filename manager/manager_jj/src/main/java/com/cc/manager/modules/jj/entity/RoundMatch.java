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
@TableName(schema = "persie_deamon", value = "round_match")
public class RoundMatch implements BaseCrudEntity<RoundMatch> {

    private static final long serialVersionUID=1L;

    /**
     * 比赛编号
     */
    @TableId(value = "ddCode", type = IdType.AUTO)
    private Integer ddCode;

    /**
     * 比赛名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 游戲狀態
     */
    @TableField("ddState")
    private Boolean ddState;

    /**
     * 游戏编号
     */
    @TableField("ddGame")
    private Integer ddGame;

    /**
     * 指定产品
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 赛场编号
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
     * 赛场资源文件
     */
    @TableField("ddRes")
    private String ddRes;

    /**
     * 产品匹配版本号
     */
    @TableField("ddMatchVersion")
    private String ddMatchVersion;

    /**
     * 赛场创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ddTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime ddTime;


    /**
     * 展示数据-产品名称
     */
    @TableField(exist = false)
    private  String appName;
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
    /**
     * 展示数据-现金赛图片0
     */
    @TableField(exist = false)
    private  String gamePicture0;

    /**
     * 展示数据-现金赛图片1
     */
    @TableField(exist = false)
    private  String gamePicture1;

    @Override
    public String getCacheKey() {
        return this.ddCode.toString();
    }

    @Override
    public String getCacheValue() {
        return this.ddCode + "-" + this.ddName;
    }
}
