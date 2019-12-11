package com.fish.service;

import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RankingRecordService implements BaseService<RankingRecord> {

   @Autowired
    RankingRecordMapper rankingRecordMapper;
   @Autowired
    GameRoundMapper gameRoundMapper;
   @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
   @Autowired
    RoundsMapper roundsMapper;
   @Autowired
    UserInfoMapper userInfoMapper;
   @Override
    //查询排名信息
    public List<RankingRecord> selectAll(GetParameter parameter) {

       List<ArcadeGames> games = arcadeGamesMapper.selectAll();
       for (ArcadeGames game : games) {
           Integer ddcode = game.getDdcode();
           List<GameRound> gameRounds = gameRoundMapper.selectByDdCode(ddcode);
           for (GameRound gameRound : gameRounds) {
               String ddround = gameRound.getDdround();
           }
       }
       List<GameRound> gameRounds = gameRoundMapper.selectAll();
        for (GameRound gameRound : gameRounds) {
            Integer ddgame = gameRound.getDdgame();
            String ddround = gameRound.getDdround();
            Date ddstart = gameRound.getDdstart();
            ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(ddgame);
            String gameName = arcadeGames.getDdname();//游戏名称
            Rounds rounds = roundsMapper.selectByDdCodeS(ddround);


                String roundName = rounds.getDdname();
            //rankingRecordMapper.selectExport(roundName,ddgame,0,ddstart);

        }
        List<RankingRecord> appConfigs = rankingRecordMapper.selectAll();
       //ddMCode  ddGCode   ddMIndex  ddMDate

       return appConfigs;
    }


    public int insert(RankingRecord record) {

        return rankingRecordMapper.insert(record);
    }


    public int updateByPrimaryKeySelective(RankingRecord record) {
        record.setDdtime(new Timestamp(new Date().getTime()));
        return rankingRecordMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<RankingRecord> getClassInfo() {
        return RankingRecord.class;
    }

    @Override
    public boolean removeIf(RankingRecord record, Map<String, String> searchData) {

//        if (existValueFalse(searchData.get("appId"), appConfig.getDdappid())) {
//            return true;
//        }

//        if (existValueFalse(searchData.get("gameName"), appConfig.getDdname())) {
//            return true;
//        }
        return true;
    }
}
