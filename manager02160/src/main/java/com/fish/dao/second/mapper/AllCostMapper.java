package com.fish.dao.second.mapper;

import com.fish.dao.second.model.AllCost;
import com.fish.protocols.MatchCost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllCostMapper {

    List<AllCost> selectAllCost(@Param("start") String start, @Param("end") String end);

    List<MatchCost> selectBySQL(String sql);

    AllCost selectCurrentCoin(String ddtime);

}