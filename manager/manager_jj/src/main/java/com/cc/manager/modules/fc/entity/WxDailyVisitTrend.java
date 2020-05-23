package com.cc.manager.modules.fc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author cf
 * @since 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxDailyVisitTrend implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * appId
     */
    @TableField("appId")
    private String appId;

    /**
     * 日期
     */
    @TableField("refDate")
    private String refDate;

    /**
     * 打开次数
     */
    @TableField("sessionCnt")
    private Integer sessionCnt;

    /**
     * 访问次数
     */
    @TableField("visitPv")
    private Integer visitPv;

    /**
     * 访问人数
     */
    @TableField("visitUv")
    private Integer visitUv;

    /**
     * 新用户数
     */
    @TableField("visitUvNew")
    private Integer visitUvNew;

    /**
     * 人均停留时长（单位：秒）
     */
    @TableField("stayTimeUv")
    private Double stayTimeUv;

    /**
     * 次均停留时长（单位：秒）
     */
    @TableField("stayTimeSession")
    private Double stayTimeSession;

    /**
     * 平均访问深度
     */
    @TableField("visitDepth")
    private Double visitDepth;

    /**
     * 数据插入时间
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;


}
