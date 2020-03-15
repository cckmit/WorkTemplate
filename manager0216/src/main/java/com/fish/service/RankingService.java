package com.fish.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.primary.model.RoundRecord;
import com.fish.dao.primary.model.ShowRanking;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ExportResult;
import com.fish.utils.RedisUtil;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 比赛结果  Service
 * RankingService
 *
 * @author
 * @date
 */
@Service
public class RankingService implements BaseService<ShowRanking> {

    private RankingMapper rankingMapper;
    private RankingRecordMapper rankingRecordMapper;
    private ArcadeGamesMapper arcadeGamesMapper;

    private RedisUtil redisUtil;
    private UserInfoMapper userInfoMapper;
    private RoundExtMapper roundExtMapper;
    private RoundRecordMapper roundRecordMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询排名信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ShowRanking> selectAll(GetParameter parameter) {
        List<RoundRecord> roundRecords;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("times").isEmpty()) {
            roundRecords = roundRecordMapper.selectSRank();
        } else {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            roundRecords = roundRecordMapper.selectSRankTime(format.format(parse[0]), format.format(parse[1]));
        }
        ArrayList<ShowRanking> shows = new ArrayList<>();
        Date roundDate;
        ShowRanking showRanking;
        if (roundRecords.size() > 0) {
            for (RoundRecord roundRecord : roundRecords) {
                roundDate = roundRecord.getDdsubmit();
                showRanking = new ShowRanking();
                showRanking.setDdCode(roundRecord.getDdcode());
                showRanking.setDdGroup(roundRecord.getDdgroup());
                showRanking.setDdIndex(roundRecord.getDdindex());
                showRanking.setGamesCode(roundRecord.getDdgame());
                showRanking.setDdNumber(roundRecord.getDdresult());
                showRanking.setMatchdate(roundDate);
                Integer ddCode = roundRecord.getDdgame();
                ArcadeGames arcadeGames = cacheService.getArcadeGames(ddCode);
                if (arcadeGames != null) {
                    String gameName = arcadeGames.getDdname();
                    showRanking.setGamesName(gameName);
                }
                String ddRound = roundRecord.getDdround();
                RoundExt roundExt = cacheService.getRoundExt(ddRound);
                if (roundExt != null) {
                    showRanking.setRoundCode(ddRound);
                    String roundName = roundExt.getDdname();
                    showRanking.setRoundName(roundName);
                    String tip = roundExt.getTip();
                    showRanking.setRoundLength(tip);
                }
                shows.add(showRanking);
            }
        }
        return shows;
    }

    /**
     * 导出Excel结果
     *
     * @param productInfo
     * @return
     */
    public List<ExportResult> selectResult(ShowRanking productInfo) {

        List<ExportResult> exportResults = new ArrayList<>();
        //获取赛场信息
        Integer ddCode = productInfo.getDdCode();
        Boolean ddGroup = productInfo.getDdGroup();
        Integer ddNumber = productInfo.getDdNumber();
        Date matchdate = productInfo.getMatchdate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(matchdate);
        String roundCode = productInfo.getRoundCode();
        String roundName = productInfo.getRoundName();
        String roundLength = productInfo.getRoundLength();
        int group = 0;
        int num = 0;
        if (ddGroup) {
            group = 1;
        } else {
            group = 0;
        }
        if (ddNumber != null) {
            int numbers = ddNumber;
            if (numbers <= 100) {
                num = 0;
            } else {
                num = numbers / 100;
            }
        }
        Integer ddIndex = productInfo.getDdIndex();
        String matchRes = baseConfig.getMatchRes();
        JSONArray allResult = new JSONArray();
        //获取比赛结果信息
        for (int i = 0; i <= num; i++) {
            String obtainResultUrl = matchRes + "match-c" + ddCode + "-g" + group + "-i" + ddIndex + "-" + i + ".json";
            String result = HttpUtil.get(obtainResultUrl);
            JSONArray singleResult = JSONArray.parseArray(result);
            allResult.addAll(singleResult);
        }
        for (Object object : allResult) {
            //封装每条比赛结果
            ExportResult exportResult = new ExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
            Object value = jsonObject.get("value");
            Object type = jsonObject.get("type");
            Object mark = jsonObject.get("mark");
            int reward = Integer.parseInt(value.toString());
            if (type.toString().equals("rmb")) {
                reward = reward / 100;
            }
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setIndex(Integer.parseInt(index.toString()));
            exportResult.setName(name.toString());
            exportResult.setUid(uid.toString());
            exportResult.setValue(reward);
            exportResult.setType(type.toString());
            exportResult.setMark(Integer.parseInt(mark.toString()));
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setMatchdate(format);
            exportResults.add(exportResult);
        }
        return exportResults;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("matchdate");
    }

    @Override
    public Class<ShowRanking> getClassInfo() {
        return ShowRanking.class;
    }

    @Override
    public boolean removeIf(ShowRanking record, JSONObject searchData) {
        if (existValueFalse(searchData.getString("gameName"), record.getGamesCode())) {
            return true;
        }
        String roundName = searchData.getString("roundName");
        if (roundName != null && roundName.contains("-"))
            roundName = roundName.split("-")[0];
        return (existValueFalse(roundName, record.getRoundCode()));
    }

    @Autowired
    public RankingService(RankingMapper rankingMapper, RankingRecordMapper rankingRecordMapper, GameRoundMapper gameRoundMapper, ArcadeGamesMapper arcadeGamesMapper, RoundsMapper roundsMapper, RedisUtil redisUtil, UserInfoMapper userInfoMapper, RoundExtMapper roundExtMapper, RoundRecordMapper roundRecordMapper, ShowRanking showRanking) {
        this.rankingMapper = rankingMapper;
        this.rankingRecordMapper = rankingRecordMapper;
        this.arcadeGamesMapper = arcadeGamesMapper;
        this.redisUtil = redisUtil;
        this.userInfoMapper = userInfoMapper;
        this.roundExtMapper = roundExtMapper;
        this.roundRecordMapper = roundRecordMapper;
    }

}
