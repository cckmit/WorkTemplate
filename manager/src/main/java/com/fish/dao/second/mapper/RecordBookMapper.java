package com.fish.dao.second.mapper;

import com.fish.dao.second.model.RecordBook;

import java.util.List;

public interface RecordBookMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(RecordBook record);

    int insertSelective(RecordBook record);

    RecordBook selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RecordBook record);

    int updateByPrimaryKey(RecordBook record);

    List<RecordBook> selectAll();
}