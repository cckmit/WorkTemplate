package com.cc.manager.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;

/**
 * 数据字典
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-24 16:48
 */
@Data
@TableName(schema = "manager_system", value = "sys_data_dict")
public class DataDict implements BaseCrudEntity<DataDict> {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    /**
     * 数据类型
     */
    @TableField(value = "data_type", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String dataType;
    /**
     * key
     */
    @TableField(value = "data_key", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String dataKey;
    /**
     * value
     */
    @TableField(value = "data_value", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String dataValue;
    /**
     * 描述
     */
    @TableField(value = "data_description")
    private String dataDescription;
    /**
     * 排序
     */
    @TableField(value = "data_sort")
    private int dataSort;
    /**
     * 是否有效
     */
    @TableField(value = "status", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private boolean status = false;
    /**
     * 数据更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "update_time")
    private Date updateTime;

    @Override
    public String getCacheKey() {
        return Arrays.toString(new String[]{this.dataType, this.dataKey});
    }

    @Override
    public String getCacheValue() {
        return this.dataValue;
    }
}
