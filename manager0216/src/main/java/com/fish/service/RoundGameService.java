package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.RoundExtMapper;
import com.fish.dao.primary.mapper.RoundGameMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.RoundExt;
import com.fish.dao.primary.model.RoundGame;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 常规赛制 Service
 * RoundGameService
 *
 * @author
 * @date
 */
@Service
public class RoundGameService implements BaseService<RoundGame> {

    @Autowired
    RoundGameMapper roundGameMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    RoundExtMapper roundExtMapper;
    @Autowired
    RoundGame roundGame;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

    /**
     * 查询赛制
     *
     * @param parameter
     * @return
     */
    @Override
    public List<RoundGame> selectAll(GetParameter parameter) {
        String url = baseConfig.getResHost();
        List<RoundGame> roundGames = roundGameMapper.selectAll();
        for (RoundGame roundGame : roundGames) {
            Integer ddgame = roundGame.getDdgame();
            ArcadeGames arcadeGames = cacheService.getArcadeGames(ddgame);
            if (arcadeGames != null) {
                String gameName = arcadeGames.getDdname();
                roundGame.setGameName(gameName);
                String ddShareRes = arcadeGames.getDdshareres();
                if (StringUtils.isNotEmpty(ddShareRes)) {
                    if (StringUtils.isNotEmpty(ddShareRes)) {
                        JSONArray shareLists = JSONObject.parseArray(ddShareRes);
                        for (int i = 0; i < shareLists.size(); i++) {
                            JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                            if (jsonObject.getInteger("position") == 3) {
                                String icon = WxConfigService.concatUrl(url, jsonObject.getString("url"), "g" + arcadeGames.getDdcode(), "share");
                                if (icon != null)
                                    roundGame.setJumpDirect(icon);
                            }
                        }
                    }
                }
            }
            String ddround = roundGame.getDdround();
            RoundExt roundExt = cacheService.getRoundExt(ddround);
            String ddreward = roundExt.getDdreward();
            String roundName = roundExt.getDdname();
            roundGame.setRoundName(roundName);
            roundGame.setDdreward(ddreward);
        }
        return roundGames;
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    public int insert(RoundGame record) {
        updateRoundGame(record);
        return roundGameMapper.insert(record);
    }

    /**
     * 更新
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(RoundGame record) {
        updateRoundGame(record);
        return roundGameMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 默认排序
     *
     * @param parameter
     * @return
     */
    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("times");
        parameter.setOrder("desc");
    }

    @Override
    public Class<RoundGame> getClassInfo() {
        return RoundGame.class;
    }

    /**
     * 筛选
     *
     * @param recharge
     * @return
     */
    @Override
    public boolean removeIf(RoundGame recharge, JSONObject searchData) {
        String times = searchData.getString("times");
        Date[] parse = XwhTool.parseDate(times);
        if (times != null && times.length() != 0) {
            if (recharge.getDdend().before(parse[0]) || recharge.getDdend().after(parse[1])) {
                return true;
            }
        }
        if (existValueFalse(searchData.getString("gameName"), recharge.getGameName())) {
            return true;
        }
        if (existValueFalse(searchData.getString("ddstate"), recharge.getDdstate().toString())) {
            return true;
        }
        return (existValueFalse(searchData.getString("roundName"), recharge.getRoundName()));
    }

    /**
     * 查询所有游戏赛制
     *
     * @return
     */
    public List<RoundExt> selectAllS() {
        List<RoundExt> roundExt = roundExtMapper.selectAllS();
        for (RoundExt round : roundExt) {
            String ddCode = round.getDdcode();
            String ddName = round.getDdname();
            round.setDdcode(ddCode + "-" + ddName);
        }
        return roundExt;
    }

    /**
     * 更新或新增时统一处理（提取的之前的老的代码块）
     *
     * @param roundGame
     */
    private void updateRoundGame(RoundGame roundGame) {

        if (!StringUtils.isBlank(roundGame.getGameCodeSelect())) {
            roundGame.setDdgame(Integer.parseInt(roundGame.getGameCodeSelect()));
        }
        if (!StringUtils.isBlank(roundGame.getRoundSelect())) {
            String[] roundSplit = roundGame.getRoundSelect().split("-");
            if (roundSplit.length > 0) {
                roundGame.setDdround(roundSplit[0]);
                roundGame.setDdname(roundSplit[1]);
            }
        }
        roundGame.setTimes(new Timestamp(System.currentTimeMillis()));
    }
}
