package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.RoundReceiveMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cf
 * @since 2020-05-12
 */
@Service
@DS("jj")
public class RoundReceiveService extends BaseCrudService<RoundReceive, RoundReceiveMapper> {

    private GamesService gamesService;
    private UserInfoService userInfoService;
    private RoundExtService roundExtService;

    private RoundMatchService roundMatchService;
    private RoundGameService roundGameService;

    private Map<String, JSONObject> roundInfoMap = new ConcurrentHashMap<>();

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundReceive> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String gameName = queryObject.getString("gameName");
            queryWrapper.eq(StringUtils.isNotBlank(gameName), "ddGame", gameName);
            String roundName = queryObject.getString("roundName");
            queryWrapper.eq(StringUtils.isNotBlank(roundName), "ddRound", roundName);

        } else {
            String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            queryWrapper.between("DATE(ddTime)", beginTime, endTime);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<RoundReceive> entityList) {
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        for (RoundReceive roundReceive : entityList) {
            String ddUid = roundReceive.getDdUid();
            String ddType = roundReceive.getDdType();
            if ("rmb".equals(ddType)) {
                roundReceive.setDdTotal(roundReceive.getDdTotal() / 100);
            }
           //UserInfo userInfo = this.userInfoService.getCacheEntity(UserInfo.class, ddUid);
            UserInfo userInfo = this.userInfoService.getById(ddUid);
            roundReceive.setUserName(userInfo.getDdName());
            if (queryData != null) {
                String userName = queryData.getString("userName");
                if (StringUtils.isNotBlank(userName)) {
                    if (!userInfo.getDdName().contains(userName)) {
                        continue;
                    }
                }
            }
            Games games = this.gamesService.getCacheEntity(Games.class, roundReceive.getDdGCode().toString());
            if (games != null) {
                roundReceive.setGameName(games.getDdName());
            } else {
                roundReceive.setGameName("未知");
            }
            JSONObject roundInfo = getRoundInfo(roundReceive.getDdGroup(), roundReceive.getDdMCode());
            if (roundInfo.containsKey("name")) {
                roundReceive.setRoundName(roundInfo.getString("name"));
            } else {
                roundReceive.setRoundName("未知比赛");
            }
            if (roundInfo.containsKey("time")) {
                roundReceive.setRoundTime(roundInfo.getString("time"));
            } else {
                roundReceive.setRoundTime("未知时长");
            }
            if ((roundInfo.containsKey("code"))) {
                roundReceive.setRoundCode(roundInfo.getString("code"));
            } else {
                roundReceive.setRoundCode("未知赛制");
            }
        }
    }

    /**
     * 获取赛制信息
     *
     * @param isGroup 是否群编号
     * @param ddmCode 赛制编号
     * @return 赛制信息
     */
    private JSONObject getRoundInfo(boolean isGroup, int ddmCode) {
        String key = MessageFormat.format("match-{0}-g{1}", isGroup, ddmCode);
        if (roundInfoMap.containsKey(key)) {
            return roundInfoMap.get(key);
        }
        JSONObject data = new JSONObject();
        String ddRound = null;
        if (isGroup) {
            RoundMatch roundGroup = this.roundMatchService.getById(ddmCode);
            if (roundGroup != null) {
                data.put("name", roundGroup.getDdName());
                ddRound = roundGroup.getDdRound();
            }
        } else {
            RoundGame roundGame = this.roundGameService.getById(ddmCode);
            if (roundGame != null) {
                data.put("name", roundGame.getDdName());
                ddRound = roundGame.getDdRound();
            }
        }
        if (ddRound != null) {
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, ddRound);
            if (roundExt != null) {
                data.put("time", roundExt.getTip());
                data.put("code", roundExt.getId());
            }
        }
        roundInfoMap.put(key, data);
        return data;
    }

    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setRoundExtService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

    @Autowired
    public void setRoundMatchService(RoundMatchService roundMatchService) {
        this.roundMatchService = roundMatchService;
    }

    @Autowired
    public void setRoundGameService(RoundGameService roundGameService) {
        this.roundGameService = roundGameService;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundReceive> deleteWrapper) {
        return false;
    }

}
