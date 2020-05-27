package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie_deamon", value = "supplement_order")
public class SupplementOrder implements BaseCrudEntity<SupplementOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 用户昵称
     */
    @TableField("userName")
    private String userName;

    /**
     * 来源AppId
     */
    @TableField("appId")
    private String appId;

    /**
     * 来源产品名称
     */
    @TableField("appName")
    private String appName;

    /**
     * 来源类型
     */
    private Integer programType;

    /**
     * 增加金币
     */
    private Integer coinCount;

    /**
     * 插入时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private long currentCoin;

}
