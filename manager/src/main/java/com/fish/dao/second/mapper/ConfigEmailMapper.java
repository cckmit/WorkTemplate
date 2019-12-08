package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigEmail;

import java.util.List;

public interface ConfigEmailMapper
{
    int deleteByPrimaryKey(String udptype);

    int insert(ConfigEmail record);

    int insertSelective(ConfigEmail record);

    ConfigEmail selectByPrimaryKey(String udptype);

    int updateByPrimaryKeySelective(ConfigEmail record);

    int updateByPrimaryKeyWithBLOBs(ConfigEmail record);

    int updateByPrimaryKey(ConfigEmail record);

    List<ConfigEmail> selectAll();
}