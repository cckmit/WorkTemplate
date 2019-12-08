package com.fish.dao.second.mapper;

import com.fish.dao.second.model.WxConfig;

import java.util.List;

public interface WxConfigMapper {
    int deleteByPrimaryKey(String ddappid);

    int insert(WxConfig record);

    int insertSelective(WxConfig record);

    WxConfig selectByPrimaryKey(String ddappid);

    WxConfig selectByProductName(String productName);

    int updateByPrimaryKeySelective(WxConfig record);

    int updateByPrimaryKey(WxConfig record);
    List<WxConfig> selectAll();
}