package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigSubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigSubscribeMapper {
    int deleteByPrimaryKey(@Param("ddAppId") String ddAppId, @Param("ddId") String ddId);

    int insert(ConfigSubscribe record);

    ConfigSubscribe selectByPrimaryKey(@Param("ddAppId") String ddAppId, @Param("ddId") String ddId);

    int updateByPrimaryKeySelective(ConfigSubscribe record);

    List<ConfigSubscribe> selectAll();
}