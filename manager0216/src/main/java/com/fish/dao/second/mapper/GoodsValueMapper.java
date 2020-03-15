package com.fish.dao.second.mapper;

import com.fish.dao.second.model.GoodsValue;

import java.util.List;

public interface GoodsValueMapper {
    int deleteByPrimaryKey(Integer ddId);

    int insertSelective(GoodsValue record);

    GoodsValue selectByPrimaryKey(Integer ddId);

    int updateByPrimaryKeySelective(GoodsValue record);

    List<GoodsValue> selectAll();
}