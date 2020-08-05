package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxVisitDistribution;
import org.springframework.stereotype.Repository;

/**
 * WxVisitDistribution Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 17:58
 */
@Repository
public interface WxVisitDistributionMapper {

    /**
     * 插入用户小程序访问分布数据
     *
     * @param wxVisitDistribution 用户小程序访问分布数据
     * @return id
     */
    int insert(WxVisitDistribution wxVisitDistribution);

    /**
     * 删除用户小程序访问分布数据
     *
     * @param wxVisitDistribution 用户小程序访问分布数据
     * @return 删除结果
     */
    int delete(WxVisitDistribution wxVisitDistribution);

}
