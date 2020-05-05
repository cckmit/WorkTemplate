package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ArcadeGames;

import java.util.List;

public interface ArcadeGamesMapper {

    List<ArcadeGames> selectAll();

    int insert(ArcadeGames record);

    ArcadeGames selectByPrimaryKey(Integer ddCode);

    int updateByPrimaryKeySelective(ArcadeGames record);

    int updateGameBySelective(ArcadeGames record);

    int insertGameInfo(ArcadeGames record);

    int updateSQL(String SQL);

}