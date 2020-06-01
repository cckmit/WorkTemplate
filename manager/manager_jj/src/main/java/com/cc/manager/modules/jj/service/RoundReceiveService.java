package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.RoundReceiveMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    private final static Map<String, JSONObject> roundInfoMap = new ConcurrentHashMap<>();

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<RoundReceive> queryWrapper, StatsListResult statsListResult) {
        // 初始化时间，如果没有时间，默认查询当日数据
        String beginTime = null, endTime = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginTime = timeRangeArray[0].trim();
            endTime = timeRangeArray[1].trim();
        }
        if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
            beginTime = endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        queryWrapper.between("DATE(ddTime)", beginTime, endTime);

        // 其他请求参数
        String gameName = statsListParam.getQueryObject().getString("gameName");
        queryWrapper.eq(StringUtils.isNotBlank(gameName), "ddGCode", gameName);
        String ddGroup = statsListParam.getQueryObject().getString("ddGroup");
        queryWrapper.eq(StringUtils.isNotBlank(ddGroup), "ddGroup", Boolean.parseBoolean(ddGroup));

        // 根据用户名模糊匹配查询
        String userName = statsListParam.getQueryObject().getString("userName");
        List<UserInfo> userInfoList = this.userInfoService.getUserInfoListByUserName(userName);
        List<String> uidList = userInfoList.stream().map(UserInfo::getDdUid).collect(Collectors.toList());
        queryWrapper.in(!uidList.isEmpty(), "ddUid", uidList);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<RoundReceive> entityList, StatsListResult statsListResult) {
        List<RoundReceive> newList = new ArrayList<>();
        if (!entityList.isEmpty()) {
            Set<String> uuidSet = entityList.stream().map(RoundReceive::getDdUid).collect(Collectors.toSet());
            List<UserInfo> userInfoList = this.userInfoService.getUserInfoListByUuidList(uuidSet);
            Map<String, String> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getDdUid, UserInfo::getDdName));
            for (RoundReceive roundReceive : entityList) {
                // 更新金钱单位
                if (StringUtils.equals("rmb", roundReceive.getDdType())) {
                    roundReceive.setDdTotal(roundReceive.getDdTotal() / 100);
                }
                // 更新用户名
                roundReceive.setUserName(userInfoMap.get(roundReceive.getDdUid()));
                // 更新游戏名称
                roundReceive.setGameName(this.gamesService.getCacheValue(Games.class, String.valueOf(roundReceive.getDdGCode())));
                // 更新赛场信息
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
                // 根据查询条件排除
                if ((roundInfo.containsKey("code"))) {
                    String roundName = statsListParam.getQueryObject().getString("roundName");
                    if (StringUtils.isNotBlank(roundName)) {
                        if (StringUtils.equals(roundName, roundInfo.getString("code"))) {
                            roundReceive.setRoundCode(roundInfo.getString("code"));
                            newList.add(roundReceive);
                        }
                    } else {
                        newList.add(roundReceive);
                    }
                }
            }
        }

        entityList.clear();
        entityList.addAll(newList);
        return null;
    }

    /**
     * 获取赛制信息
     *
     * @param isGroup 是否群编号
     * @param ddmCode 赛制编号
     * @return 赛制信息
     */
    private JSONObject getRoundInfo(boolean isGroup, int ddmCode) {
        // TODO
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
