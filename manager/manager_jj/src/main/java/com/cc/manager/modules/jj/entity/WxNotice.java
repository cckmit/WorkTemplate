package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author cf
 * @since 2020-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "persie_value", value = "wx_notice")
public class WxNotice extends BaseStatsEntity<WxNotice> {

    private static final long serialVersionUID = 1L;

    /**
     * AppId
     */
    @TableField("appId")
    private String appId;

    /**
     * ID
     */
    @TableId("noticeId")
    private String noticeId;

    /**
     * 通知时间戳
     */
    private Long createTime;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否被标记
     */
    private Boolean isMarked;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean isReaded;

    /**
     * 数据插入时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private LocalDateTime saveTime;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(exist = false)
    private Date noticeTime;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
