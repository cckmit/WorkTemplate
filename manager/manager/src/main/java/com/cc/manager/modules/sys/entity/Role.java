package com.cc.manager.modules.sys.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.util.Date;

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
     * 角色权限菜单列表
     */
    @TableField(value = "menu_ids", insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String menuIds;
    /**
     * 数据更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 已授权页面树形数据
     */
    @TableField(exist = false)
    private JSONObject authorizedMenu;

}
