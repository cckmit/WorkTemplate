package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxUserPortrait;
import org.springframework.stereotype.Repository;

/**
 * WxUserPortrait Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-08 00:30
 */
@Repository
public interface WxUserPortraitMapper {

    /**
     * 新增用户画像数据
     *
     * @param wxUserPortrait 用户画像数据
     */
    void insert(WxUserPortrait wxUserPortrait);

    /**
     * 删除用户画像数据
     *
     * @param wxUserPortrait 用户画像数据
     * @return 删除结果
     */
    int delete(WxUserPortrait wxUserPortrait);

}
