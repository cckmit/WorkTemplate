package com.fish.dao.second.mapper;

import com.fish.dao.second.model.AppConfig;

import java.util.List;

public interface AppConfigMapper {

    int insert(AppConfig record);

    AppConfig selectByPrimaryKey(String ddAppId);

    int updateByPrimaryKeySelective(AppConfig record);

    List<AppConfig> selectAll();

    int deleteByPrimaryKey(String ddAppId);
}