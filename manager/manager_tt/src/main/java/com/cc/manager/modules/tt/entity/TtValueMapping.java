package com.cc.manager.modules.tt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author cf
 * @since 2020-07-09
 */
@Data
@TableName(schema = "tt_data_analysis", value = "value_mapping")
public class TtValueMapping implements BaseCrudEntity<TtValueMapping> {

    private static final long serialVersionUID = 1L;

    /**
     * 数据key类型
     */
    @TableId("groupByKey")
    private String groupByKey;

    /**
     * 数据映射后分组名称
     */
    @TableField("groupByValue")
    private String groupByValue;

    /**
     * 数据更新时间
     */
    @TableField("insertTime")
    private Date insertTime;

}
