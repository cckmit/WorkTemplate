package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.*;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.model.UserInfo;
import com.fish.protocols.GetParameter;
import com.fish.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GroupRankingService implements BaseService<ShowRanking> {


    @Autowired
    RankingMapper rankingMapper;
    @Autowired
    RankingRecordMapper rankingRecordMapper;
    @Autowired
    GameRoundMapper gameRoundMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    RoundsMapper roundsMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    //查询排名信息
    public List<ShowRanking> selectAll(GetParameter parameter) {
        ArrayList<ShowRanking> shows = new ArrayList<>();
//       LocalDateTime dateTime = LocalDateTime.now();
//       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//       System.out.println(dateTime.format(formatter));

        Date date = new Date();//获得系统时间. 
        String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(date);

        List<Ranking> rankings = rankingMapper.selectByDateGroup(nowTime);

        for (Ranking ranking : rankings) {
            ShowRanking showRanking = new ShowRanking();
            String matchid = ranking.getMatchid();
            Rounds rounds = roundsMapper.selectByDdCodeQ(matchid);
            String formatName = rounds.getDdname();//根据matchID获取赛制名称
            Integer gamecode = ranking.getGamecode();
            ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(gamecode);
            String gamesName = arcadeGames.getDdname();//根据游戏代号获取游戏名称
            Date matchdate = ranking.getMatchdate();//比赛日期
            Integer matchindex = ranking.getMatchindex();//场次索引

            String uid = ranking.getUid();
            Long mark = ranking.getMark();
            Long userRanking = ranking.getRanking();
            showRanking.setRanking(ranking);
            showRanking.setGamesName(gamesName);
            showRanking.setFormatName(formatName);
            shows.add(showRanking);
        }
        return shows;
    }

    public List<Ranking> selectResult(String productInfo) {
        JSONObject parse = (JSONObject) JSONObject.parse(productInfo);
        String matchId1 = parse.getString("matchId");
        String matchDate1 = parse.getString("matchDate");
        String gameCode1 = parse.getString("gameCode");
        String matchIndex1 = parse.getString("matchIndex");

        String matchId ="RD51648592" ;
        String   matchDate ="2019-12-12";
        int gameCode =1005;
        int matchIndex =0;

       List<Ranking> ranks=rankingMapper.selectGroupResult(matchId,matchDate,gameCode,matchIndex);
       if(ranks.size()>0){
           for (Ranking rank : ranks) {
               Integer gamecode = rank.getGamecode();
               ArcadeGames arcadeGames = arcadeGamesMapper.selectByPrimaryKey(gamecode);
               String gameName = arcadeGames.getDdname();
               Rounds rounds = roundsMapper.selectByDdCodeQ(rank.getMatchid());
               String formatName = rounds.getDdname();
               //场次
               Integer matchindex = rank.getMatchindex();

               String  periodTime ="";
               Integer ddHour = rounds.getDdhour0();
               periodTime ="0 - "+ddHour;
               //排名
               Long ranking = rank.getRanking();

               UserInfo userInfo = userInfoMapper.selectByDdUid(rank.getUid());
               //昵称
               String userName = userInfo.getDdname();
               String ID = rank.getUid();

               String matchid = rank.getMatchid();

               Integer awardtotal = rank.getAwardtotal();
               String awardtype = rank.getAwardtype();
               if("rmb".equals(awardtype)){
                   awardtype ="元现金";
               }else {
                   awardtype ="金币";
               }
               //奖励
                String award = awardtotal+awardtype;
               rank.setUserName(userName);
               rank.setFormatName(formatName);
               rank.setGameName(gameName);
               rank.setAward(award);
               rank.setPeriodTime(periodTime+"");
           }
       }
        return ranks;
    }
    public int insert(Ranking record) {
        Object gameCode = redisUtil.get("gameCode");
        Object matchCode = redisUtil.get("matchCode");
        Object day = redisUtil.get("day");
        Object index = redisUtil.get("index");

        Object uid = redisUtil.get("ddUid");
        Object ranking = redisUtil.get("ranking");
        Object mark = redisUtil.get("score");

        Ranking rank = new Ranking();
        rank.setGamecode(Integer.parseInt(gameCode.toString()));
        rank.setMark(Long.valueOf(mark.toString()));
        rank.setMatchid(matchCode.toString());
        rank.setMatchindex(Integer.parseInt(index.toString()));
        rank.setRanking(Long.valueOf(ranking.toString()));
        rank.setUid(uid.toString());
        rank.setInserttime(new Timestamp(new Date().getTime()));
        return rankingMapper.insert(record);
    }


    public int updateByPrimaryKeySelective(RankingRecord record) {
        record.setDdtime(new Timestamp(new Date().getTime()));
        return rankingRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<ShowRanking> getClassInfo() {
        return ShowRanking.class;
    }

    @Override
    public boolean removeIf(ShowRanking record, Map<String, String> searchData) {

//        if (existValueFalse(searchData.get("appId"), appConfig.getDdappid())) {
//            return true;
//        }

//        if (existValueFalse(searchData.get("gameName"), appConfig.getDdname())) {
//            return true;
//        }
        return true;
    }


}
