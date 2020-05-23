package com.cc.manager.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.cc.manager.common.mvc.BaseCrudEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 父菜单ID
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY)
    private int pid;
    /**
     * 名称
     */
    private String title;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 链接
     */
    private String href;
    /**
     * 链接打开方式
     */
    private String target;
    /**
     * 菜单排序
     */
    private String sort;
    /**
     * 状态
     */
    private boolean status;
    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String updateTime;

}
