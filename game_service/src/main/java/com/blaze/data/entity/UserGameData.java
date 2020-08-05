package com.blaze.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-10 12:08
 */
@Data
@TableName(schema = "persie_game", value = "user_game_data")
public class UserGameData implements BaseEntity<UserGameData> {
    /**
     * openId
     */
    @TableId(type = IdType.INPUT)
    private String openId;
    /**
     * 游戏数据，Json格式
     */
    private String gameData;

    @Override
    public Serializable getCacheKey() {
        return null;
    }
}
