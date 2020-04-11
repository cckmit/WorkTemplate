package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigProgram;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigProgramMapper {
    int deleteByPrimaryKey(String ddAppId, String ddMinVer);

    int insert(ConfigProgram record);

    int insertSelective(ConfigProgram record);

    ConfigProgram selectByPrimaryKey(String ddAppId, String ddMinVer);

    int updateByPrimaryKeySelective(ConfigProgram record);

    int updateByPrimaryKey(ConfigProgram record);

    List<ConfigProgram> selectAll();
}