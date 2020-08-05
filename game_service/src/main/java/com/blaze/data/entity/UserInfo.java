package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 18:15
 */
@Data
@TableName(schema = "persie_game", value = "user_info")
public class UserInfo implements BaseEntity<UserInfo> {

    @TableId(type = IdType.INPUT)
    private String uid;

    private String appId;
    /**
     * 平台
     */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String platform;
    /**
     * openId
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像地址
     */
    private String avatarUrl;
    /**
     * 性别
     */
    private int gender;

    /**
     * 当前数据不需要缓存
     *
     * @return 缓存-key
     */
    @Override
    public Serializable getCacheKey() {
        return null;
    }

}
