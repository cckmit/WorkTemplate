package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-03 12:07
 */
@Data
@TableName(schema = "persie", value = "config_status")
public class ConfigStatus implements BaseCrudEntity<ConfigStatus> {

    @TableId(value = "ddName", type = IdType.INPUT)
    private String id;

    @TableField("ddValue")
    private String value;

    @TableField("ddDescription")
    private String description;

}
