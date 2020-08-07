package com.cc.manager.modules.tt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-08-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(schema = "tt_data_analysis", value = "message_notice")
public class TtMessageNotice extends BaseStatsEntity<TtMessageNotice> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 消息反馈时间
     */
    @TableId("sendTime")
    private String sendTime;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否读取
     */
    @TableField("isRead")
    private Boolean isRead;

    /**
     * 数据插入时间
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;


}
