package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.ReduceJsonUtil;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.entity.RoundGame;
import com.cc.manager.modules.jj.mapper.RoundGameMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-08
 */
@Service
public class RoundGameService extends BaseCrudService<RoundGame, RoundGameMapper> {
    private WxConfigService wxConfigService;
    private GamesService gamesService;
    private RoundExtService roundExtService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<RoundGame> entityList) {
        String url = baseConfig.getResHost();
        for (RoundGame roundGame : entityList) {
            Games game = this.gamesService.getCacheEntity(Games.class, roundGame.getDdGame().toString());
            if (game != null) {
                String gameName = game.getDdName();
                roundGame.setGameName(gameName);
                String ddShareRes = game.getDdShareRes();
                if (StringUtils.isNotEmpty(ddShareRes)) {
                    JSONArray shareLists = JSONObject.parseArray(ddShareRes);
                    for (int i = 0; i < shareLists.size(); i++) {
                        JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                        if (jsonObject.getInteger("position") == 3) {
                            String icon = ReduceJsonUtil.concatUrl(url, jsonObject.getString("url"), "g" + game.getDdCode(), "share");
                            if (icon != null) {
                                roundGame.setJumpDirect(icon);
                            }
                        }
                    }
                }
            }
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, roundGame.getDdRound());
            String ddreward = roundExt.getDdReward();
            String roundName = roundExt.getDdName();
            roundGame.setRoundName(roundName);
            roundGame.setDdReward(ddreward);
            // roundGame.setRoundSelect(ddround + "_" + roundName);
        }

    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundGame> queryWrapper) {
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundGame> deleteWrapper) {
        return false;
    }


    @Override
    protected void updateInsertEntity(String requestParam, RoundGame entity) {

    }

    @Autowired
    public void setGameSetService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setGameSetService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setGameSetService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

}
