package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author cf
 * @since 2020-05-08
 */
@Data
@TableName(schema = "persie_deamon", value = "gameset")
public class GameSet implements BaseCrudEntity<GameSet> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 游戏集代号信息（四位数字，唯一）
     */
    @TableField("ddCode")
    private Integer ddCode;

    /**
     * 游戏状态,0:游戏模式，1:马甲模式
     */
    @TableField("ddState")
    private Integer ddState;

    /**
     * 跳转APPID
     */
    @TableField("ddAppid")
    private String ddAppid;

    /**
     * 游戏集名称信息
     */
    @TableField("ddName")
    private String ddName;

    /**
     * 游戏集名称信息--
     */
    @TableField("ddName128u")
    private String ddName128u;

    /**
     * 游戏集排序过滤
     */
    @TableField("ddArrange512a")
    private String ddArrange512a;

    /**
     * 游戏集内容信息
     */
    @TableField("ddContent512a")
    private String ddContent512a;

    /**
     * 游戏集内容描述
     */
    @TableField("ddDesc")
    private String ddDesc;

    /**
     * 游戏集内容描述--
     */
    @TableField("ddDesc512u")
    private String ddDesc512u;

    @Override
    public String getCacheKey() {
        return this.ddCode.toString();
    }

    @Override
    public String getCacheValue() {
        return this.ddCode + "-" + this.ddName;
    }
}
