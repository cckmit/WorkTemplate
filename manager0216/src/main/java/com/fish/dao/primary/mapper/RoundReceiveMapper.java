package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.RoundReceive;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundReceiveMapper {

    int insert(RoundReceive record);

    int updateByPrimaryKeySelective(RoundReceive record);

    List<RoundReceive> selectAll();

    List<RoundReceive> selectSearchTime(String start,String end);
}