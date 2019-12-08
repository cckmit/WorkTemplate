package com.fish.dao.second.mapper;

import com.fish.dao.second.model.RecordVirtual;

import java.util.List;

public interface RecordVirtualMapper
{
    int deleteByPrimaryKey(String id);

    int insert(RecordVirtual record);

    int insertSelective(RecordVirtual record);

    RecordVirtual selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RecordVirtual record);

    int updateByPrimaryKey(RecordVirtual record);

    List<RecordVirtual> selectAll();
}