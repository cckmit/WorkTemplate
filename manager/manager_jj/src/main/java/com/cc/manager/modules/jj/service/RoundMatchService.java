package com.cc.manager.modules.jj.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.entity.RoundMatch;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.RoundMatchMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-08
 */
@Service
public class RoundMatchService extends BaseCrudService<RoundMatch, RoundMatchMapper> {

    private WxConfigService wxConfigService;
    private GamesService gamesService;
    private RoundExtService roundExtService;

    private JjConfig jjConfig;

    /**
     * 拼接链接地址
     *
     * @param icon    图标名称
     * @param suffers 拼接数列
     * @return url
     */
    private static String concatUrl(String resultUrl, String icon, String... suffers) {
        if (StringUtils.isNotBlank(icon)) {
            if (suffers != null) {
                for (String suffer : suffers) {
                    resultUrl = resultUrl.concat(suffer).concat("/");
                }
            }
            return resultUrl.concat(icon);
        }
        return null;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundMatch> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("productName");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
            String gameName = queryObject.getString("gameName");
            queryWrapper.eq(StringUtils.isNotBlank(gameName), "ddGame", gameName);
            String roundName = queryObject.getString("roundName");
            queryWrapper.eq(StringUtils.isNotBlank(roundName), "ddName", roundName);
            String ddState = queryObject.getString("ddState");
            queryWrapper.eq(StringUtils.isNotBlank(ddState), "ddStart", ddState);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<RoundMatch> entityList) {
        String url = jjConfig.getResHost();
        for (RoundMatch roundMatch : entityList) {
            // 更新游戏信息
            Games games = this.gamesService.getCacheEntity(Games.class, roundMatch.getDdGame().toString());
            if (games == null) {
                continue;
            }
            roundMatch.setGameName(games.getDdName());
            // 判断资源分享JSON字串
            if (StringUtils.isNotBlank(games.getDdShareRes())) {
                JSONArray shareLists = JSONObject.parseArray(games.getDdShareRes());
                for (int i = 0; i < shareLists.size(); i++) {
                    JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                    if (jsonObject.getInteger("position") == 4) {
                        String icon = concatUrl(this.jjConfig.getResHost(),
                                jsonObject.getString("url"), "g" + games.getDdCode(), "share");
                        if (StringUtils.isNotBlank(icon)) {
                            roundMatch.setJumpDirect(icon);
                        }
                    }
                }
            }
            // 更新赛场信息
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, roundMatch.getDdRound());
            if (roundExt != null) {
                roundMatch.setRoundName(roundExt.getDdName());
                roundMatch.setDdReward(roundExt.getDdReward());
                roundMatch.setRoundLength(roundExt.getTip());
            }
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, roundMatch.getDdAppId());
            if (wxConfig != null) {
                roundMatch.setAppName(wxConfig.getProductName());
            }
            //获取资源图片信息
            String ddRes = roundMatch.getDdRes();
            if (StringUtils.isNotBlank(ddRes)) {
                String gamePicture0;
                String gamePicture1;
                String pngName = ddRes.split("/")[ddRes.split("/").length - 1];
                if (ddRes.endsWith(".zip")) {
                    gamePicture0 = url + pngName.substring(0, pngName.lastIndexOf(".")) + "/biaoti.png";
                    gamePicture1 = url + pngName.substring(0, pngName.lastIndexOf(".")) + "/bisai.png";
                } else {
                    gamePicture0 = url + pngName + "/biaoti.png";
                    gamePicture1 = url + pngName + "/bisai.png";
                }
                roundMatch.setGamePicture0(gamePicture0);
                roundMatch.setGamePicture1(gamePicture1);
            }
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, RoundMatch entity) {
        if (entity.getDdState() == null) {
            entity.setDdState(false);
        }
        updateOrInsertRoundMatch(entity);
    }

    @Override
    protected boolean update(String requestParam, RoundMatch entity, UpdateWrapper<RoundMatch> updateWrapper) {
        if (entity.getDdState() == null) {
            entity.setDdState(false);
        }
        updateOrInsertRoundMatch(entity);
        updateWrapper.eq("ddCode", entity.getId());
        return this.update(entity, updateWrapper);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundMatch> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            String list = StrUtil.sub(requestParam, 1, -1);
            List<String> idList = Lists.newArrayList(StringUtils.split(list, ","));
            return this.removeByIds(idList);
        }
        return false;
    }

    /**
     * 处理小程序比赛时间及分享资源
     *
     * @param record record
     */
    private void updateOrInsertRoundMatch(RoundMatch record) {
        if (record.getDdGame() != null) {
            long time;
            //获取游戏信息
            Games games = this.gamesService.getCacheEntity(Games.class, record.getDdGame().toString());
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, record.getDdRound());
            if (games.getDdIsPk() == 1) {
                //设置Pk类型结束时间
                time = record.getDdStart().toEpochSecond(ZoneOffset.of("+8")) + roundExt.getDdTime() + 300;
            } else {
                //设置不为Pk类型结束时间
                time = record.getDdStart().toEpochSecond(ZoneOffset.of("+8")) + roundExt.getDdTime();
            }
            record.setDdEnd(LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(8)));
        }
        if (StringUtils.isNotBlank(record.getDdRes())) {
            if (!record.getDdRes().startsWith("https")) {
                String resHost = this.jjConfig.getResHost();
                record.setDdRes(resHost + record.getDdRes() + "/");
            }
        }
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
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
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
