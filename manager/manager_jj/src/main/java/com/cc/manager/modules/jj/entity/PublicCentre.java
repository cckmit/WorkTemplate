package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * @author cf
 * @since 2020-05-13
 */
@Data
@TableName(schema = "persie_deamon", value = "public_centre")
public class PublicCentre implements BaseCrudEntity<PublicCentre> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 位置
     */
    private Integer locationId;

    /**
     * 展示位顺序
     */
    private Integer showId;

    /**
     * 0-banner,1-热门,2-游戏
     */
    private Integer recommendType;

    /**
     * 推荐为名称
     */
    private String recommendName;

    /**
     * 跳转类型,0-链接,1-合集
     */
    private Integer skipType;

    /**
     * 跳转合集
     */
    private Integer skipSet;

    /**
     * banner类型，0-url，1-banner
     */
    private Integer bannerType;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源详情名称
     */
    private String detailName;


}
