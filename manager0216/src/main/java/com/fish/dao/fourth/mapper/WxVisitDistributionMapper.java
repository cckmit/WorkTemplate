package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxVisitDistribution;

import java.util.List;

public interface WxVisitDistributionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxVisitDistribution record);

    int insertSelective(WxVisitDistribution record);

    WxVisitDistribution selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxVisitDistribution record);


    List<WxVisitDistribution> selectAll();
}