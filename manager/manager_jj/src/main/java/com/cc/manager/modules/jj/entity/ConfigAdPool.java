package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * @author cf
 * @since 2020-06-16
 */
@Data
@TableName(schema = "persie", value = "config_ad_pool")
public class ConfigAdPool implements BaseCrudEntity<ConfigAdPool> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ddId", type = IdType.AUTO)
    private Integer ddId;

    /**
     * 广告池名称
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 广告内容ID列表
     */
    @TableField("ddContentIds")
    private String ddContentIds;


}
