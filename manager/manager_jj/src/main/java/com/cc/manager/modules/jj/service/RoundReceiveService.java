package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.RoundReceiveMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cf
 * @since 2020-05-12
 */
@Service
@DS("jj")
public class RoundReceiveService extends BaseStatsService<RoundReceive, RoundReceiveMapper> {

    private GamesService gamesService;
    private RoundExtService roundExtService;
    private RoundMatchService roundMatchService;
    private RoundGameService roundGameService;
    private UserInfoService userInfoService;
    private Map<String, JSONObject> roundInfoMap = new ConcurrentHashMap<>();

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<RoundReceive> queryWrapper, StatsListResult statsListResult) {
        List<String> uidList = new ArrayList<>();
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String gameName = queryObject.getString("gameName");
            queryWrapper.eq(StringUtils.isNotBlank(gameName), "ddGCode", gameName);
            String ddGroup = queryObject.getString("ddGroup");
            queryWrapper.eq(StringUtils.isNotBlank(ddGroup), "ddGroup", Boolean.parseBoolean(ddGroup));

            String userName = queryObject.getString("userName");
            if (StringUtils.isNotBlank(userName)) {
                QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                userInfoQueryWrapper.like("ddName", userName);
                List<UserInfo> userInfos = this.userInfoService.getBaseMapper().selectList(userInfoQueryWrapper);
                for (UserInfo userInfo : userInfos) {
                    uidList.add(userInfo.getDdUid());
                }
                queryWrapper.in(uidList.size() > 1, "ddUid", uidList);
            }
        } else {
            String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            queryWrapper.between("DATE(ddTime)", beginTime, endTime);
        }
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<RoundReceive> entityList, StatsListResult statsListResult) {
        List<RoundReceive> newList = new ArrayList<>();
        for (RoundReceive roundReceive : entityList) {
            String ddUid = roundReceive.getDdUid();
            String ddType = roundReceive.getDdType();
            if ("rmb".equals(ddType)) {
                roundReceive.setDdTotal(roundReceive.getDdTotal() / 100);
            }
            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
            userInfoQueryWrapper.eq("ddUid", ddUid);
            List<UserInfo> userInfos = this.userInfoService.getBaseMapper().selectList(userInfoQueryWrapper);

            if (userInfos.size() > 0) {
                roundReceive.setUserName(userInfos.get(0).getDdName());
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
                if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
                    JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
                    String roundName = queryObject.getString("roundName");
                    if (StringUtils.isNotBlank(roundName)) {
                        if (!roundName.equals(roundInfo.getString("code"))) {
                            continue;
                        }
                    }
                }
                roundReceive.setRoundCode(roundInfo.getString("code"));
            } else {
                roundReceive.setRoundCode("未知赛制");
            }
            newList.add(roundReceive);
        }
        entityList.clear();
        entityList.addAll(newList);
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        try {
            // 初始化查询wrapper
            QueryWrapper<RoundReceive> queryWrapper = new QueryWrapper<>();
            this.updateGetListWrapper(statsListParam, queryWrapper, statsListResult);
            Page<RoundReceive> page = new Page<>(statsListParam.getPage(), statsListParam.getLimit());
            IPage<RoundReceive> entityPages = this.page(page, queryWrapper);
            if (Objects.nonNull(entityPages)) {
                List<RoundReceive> entityList = entityPages.getRecords();
                JSONObject totalRow = this.rebuildStatsListResult(statsListParam, entityList, statsListResult);
                statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
                statsListResult.setTotalRow(totalRow);
                statsListResult.setCount(entityPages.getTotal());
            }
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
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

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
    
}
