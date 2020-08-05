package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxWeeklyRetain;
import org.springframework.stereotype.Repository;

/**
 * WxWeeklyRetain Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 23:23
 */
@Repository
public interface WxWeeklyRetainMapper {

    /**
     * 插入用户访问小程序周留存
     *
     * @param wxWeeklyRetain 用户访问小程序周留存
     * @return id
     */
    int insert(WxWeeklyRetain wxWeeklyRetain);

    /**
     * 删除用户访问小程序周留存
     *
     * @param wxWeeklyRetain 用户访问小程序周留存
     * @return 删除结果
     */
    int delete(WxWeeklyRetain wxWeeklyRetain);

}
