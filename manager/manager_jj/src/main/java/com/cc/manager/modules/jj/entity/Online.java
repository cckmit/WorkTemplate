package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-22
 */
@Data
@TableName(schema = "persie_deamon", value = "online")
public class Online implements BaseCrudEntity<Online> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 统计时间
     */
    private LocalDateTime times;

    /**
     * 在线人数
     */
    private Integer online;

    /**
     * 空闲房间数
     */
    @TableField("idleRoom")
    private Integer idleRoom;

    /**
     * 在线房间数
     */
    @TableField("buzyRoom")
    private Integer buzyRoom;

    /**
     * 游戏详情
     */
    @TableField("gameInfo")
    private String gameInfo;

    /**
     * 同步时间
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;


}
