package com.cc.manager.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统菜单信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-05 11:10
 */
@Data
@TableName(schema = "manager_system", value = "sys_menu")
public class Menu implements BaseCrudEntity<Menu> {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private int parentId;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 菜单地址
     */
    @TableField("href")
    private String href;
    /**
     * 图标
     */
    @TableField("icon")
    private String icon;
    /**
     * 权限标识
     */
    @TableField("perms")
    private String perms;
    /**
     * 类型 0菜单 1按钮
     */
    @TableField("type")
    private int type;
    /**
     * 排序
     */
    @TableField("order_num")
    private int orderNum;
    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

    @Override
    public String getCacheKey() {
        return String.valueOf(this.id);
    }

    @Override
    public String getCacheValue() {
        return String.valueOf(this.name);
    }

}
