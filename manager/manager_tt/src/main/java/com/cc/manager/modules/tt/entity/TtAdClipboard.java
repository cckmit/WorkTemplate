package com.cc.manager.modules.tt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 头条剪切板广告数据
 *
 * @author cf
 * @since 2020-07-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "persie_value", value = "tt_ad_clipboard")
public class TtAdClipboard extends BaseStatsEntity<TtAdClipboard> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private Integer dateVal;

    /**
     * 平台
     */
    private String platform;

    private String appId;
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 版本
     */
    private String version;

    /**
     * 广告类型
     */
    private String adType;

    private String adStatus;

    /**
     * 数量
     */
    private Integer counts;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
