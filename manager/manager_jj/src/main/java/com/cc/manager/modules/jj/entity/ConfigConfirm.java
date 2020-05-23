package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author cf
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigConfirm implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键标志
     */
    @TableId("ddId")
    private String ddId;

    /**
     * 描述信息
     */
    private String describe;

    /**
     * 状态
     */
    @TableField("ddStatus")
    private Boolean ddStatus;

    /**
     * 确认值
     */
    @TableField("ddYes")
    private String ddYes;

    /**
     * 否认值
     */
    @TableField("ddNo")
    private String ddNo;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

}
