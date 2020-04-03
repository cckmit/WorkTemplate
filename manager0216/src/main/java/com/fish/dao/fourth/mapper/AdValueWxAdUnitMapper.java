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
     * 查询所有数据
     *
     * @return
     */
    List<AdValueWxAdUnit> selectAll(AdValueWxAdUnit adValueWxAdUnit);

    List<AdValueWxAdUnit> queryCollectData(AdValueWxAdUnit adValueWxAdUnit);

    List<AdValueWxAdUnit> selectAllScreenIncome(String beginTime, String endTime);

    List<AdValueWxAdUnit> queryScreenIncomeByDate(String beginTime, String endTime);
}
