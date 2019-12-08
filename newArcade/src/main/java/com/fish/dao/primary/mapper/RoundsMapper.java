package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.Rounds;

import java.util.List;

public interface RoundsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Rounds record);

    int insertSelective(Rounds record);

    Rounds selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Rounds record);

    int updateByPrimaryKey(Rounds record);

    List<Rounds> selectAll();

    Rounds selectByDdCode(String ddCode);
}