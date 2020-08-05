package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxDailyRetain;
import org.springframework.stereotype.Repository;

/**
 * WxDailyRetain Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:03
 */
@Repository
public interface WxDailyRetainMapper {

    /**
     * 存储小程序日留存
     *
     * @param wxDailyRetain 小程序日留存数据
     * @return id
     */
    int insert(WxDailyRetain wxDailyRetain);

    /**
     * 删除小程序日留存
     *
     * @param wxDailyRetain 小程序日留存数据
     * @return 删除结果
     */
    int delete(WxDailyRetain wxDailyRetain);

}
