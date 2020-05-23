package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Data
@TableName(schema = "persie", value = "config_program")
public class ConfigProgram implements BaseCrudEntity<ConfigProgram> {

    private static final long serialVersionUID = 1L;

    /**
     * appId
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 满足最低版本
     */
    @TableField("ddMinVer")
    private String ddMinVer;

    /**
     * 合集编号
     */
    @TableField("ddCode")
    private Integer ddCode;

    /**
     * 游戏链接
     */
    @TableField("ddUrl")
    private String ddUrl;

    /**
     * 操作时间
     */
    private LocalDateTime times;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 类型
     */
    @TableField(exist = false)
    private Integer programType;

    /**
     * 合集名称
     */
    @TableField(exist = false)
    private String codeName;

}
