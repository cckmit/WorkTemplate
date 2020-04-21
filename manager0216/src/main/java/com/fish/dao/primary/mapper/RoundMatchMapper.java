package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundMatch;

import java.util.List;

public interface RoundMatchMapper {

    int deleteByPrimaryKey(Integer ddcode);

    int insert(RoundMatch record);

    RoundMatch selectByPrimaryKey(Integer ddcode);

    int updateByPrimaryKeySelective(RoundMatch record);

    List<RoundMatch> selectAll();

}