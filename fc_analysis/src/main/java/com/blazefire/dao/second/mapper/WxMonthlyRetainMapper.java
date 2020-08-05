package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxMonthlyRetain;
import org.springframework.stereotype.Repository;

/**
 * WxMonthlyRetain Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:03
 */
@Repository
public interface WxMonthlyRetainMapper {

    /**
     * 存储小程序月留存
     *
     * @param wxMonthlyRetain 小程序月留存数据
     * @return id
     */
    int insert(WxMonthlyRetain wxMonthlyRetain);

    /**
     * 删除小程序月留存数据
     *
     * @param wxMonthlyRetain 小程序月留存数据
     * @return 删除结果
     */
    int delete(WxMonthlyRetain wxMonthlyRetain);

}
