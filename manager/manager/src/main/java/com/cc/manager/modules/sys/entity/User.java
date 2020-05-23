package com.cc.manager.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 22:45
 */
@Data
@TableName(schema = "manager_system", value = "sys_user")
public class User implements BaseCrudEntity<User> {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    @TableField(value = "username", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String username;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    @TableField(value = "password", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String password;
    /**
     * 昵称
     */
    @TableField(value = "nick_name", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String nickName;
    /**
     * 用户角色列表ID字符串
     */
    @TableField(value = "role_ids", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String roleIds;
    /**
     * 用户角色列表名字
     */
    @TableField(exist = false)
    private String roleNames;
    /**
     * 最后登录时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @TableField(value = "last_login_time", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date lastLoginTime;
    /**
     * 是否允许登录
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
        return String.valueOf(this.id);
    }

}
