package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.AdValueWxAdUnit;

import java.util.List;


/**
 * 广告接口数据分析
 *
 * @author
 * @date
 */
public interface AdValueWxAdUnitMapper {

    /**
     * 查询汇总数据
     * @param adValueWxAdUnit
     * @return
     */
    List<AdValueWxAdUnit> queryCollectData(AdValueWxAdUnit adValueWxAdUnit);
    /**
     * 查询插屏收入
     *
     * @return
     */
    List<AdValueWxAdUnit> selectAllScreenIncome(String beginTime, String endTime);
    /**
     * 查询插屏收入汇总数据
     *
     * @return
     */
    List<AdValueWxAdUnit> queryScreenIncomeByDate(String beginTime, String endTime);
}
