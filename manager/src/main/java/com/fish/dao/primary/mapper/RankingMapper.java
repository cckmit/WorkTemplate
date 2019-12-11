package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.Ranking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Ranking record);

    int insertSelective(Ranking record);

    Ranking selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Ranking record);

    int updateByPrimaryKey(Ranking record);

    int insertBatch(List<Ranking> rankings);

    Ranking selectByGameCode(int gameCode);

    List<Ranking> selectByDate(String timestamp);

    List<Ranking> selectResult(@Param("matchid")String matchId, @Param("matchdate")String matchDate, @Param("gamecode")int gameCode, @Param("matchindex")int matchIndex);


}