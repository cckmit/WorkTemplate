package com.cc.manager.modules.sys.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户角色信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 22:59
 */
@Data
@TableName(schema = "manager_system", value = "sys_role")
public class Role implements BaseCrudEntity<Role> {

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 角色名称
     */
    @TableField(value = "role_name", insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String roleName;
    /**
     * 是否有效
     */
    @TableField(value = "status", insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private boolean status;
    /**
     * 菜单列表
     */
    @TableField(value = "menu_ids", insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String menuIds;
    /**
     * 数据更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    /**
     * 角色的授权菜单树
     */
    @TableField(exist = false)
    private JSONObject menuTreeJson;

    @Override
    public String getCacheKey() {
        return String.valueOf(this.id);
    }

    @Override
    public String getCacheValue() {
        return this.roleName;
    }

}
