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
import com.cc.manager.modules.jj.entity.RoundGame;
import com.cc.manager.modules.jj.mapper.RoundGameMapper;
import com.google.common.collect.Lists;
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
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundGame> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddEnd)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String gameName = queryObject.getString("gameName");
            queryWrapper.eq(StringUtils.isNotBlank(gameName), "ddGame", gameName);
            String roundName = queryObject.getString("roundName");
            queryWrapper.eq(StringUtils.isNotBlank(roundName), "ddName", roundName);
            String ddState = queryObject.getString("ddState");
            queryWrapper.eq(StringUtils.isNotBlank(ddState), "ddState", Boolean.parseBoolean(ddState));

        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<RoundGame> entityList) {
        String url = this.jjConfig.getResHost();
        for (RoundGame roundGame : entityList) {
            Games game = this.gamesService.getCacheEntity(Games.class, roundGame.getDdGame().toString());
            if (game != null) {
                roundGame.setGameName(game.getDdName());
                String ddShareRes = game.getDdShareRes();
                if (StringUtils.isNotBlank(ddShareRes)) {
                    JSONArray shareLists = JSONObject.parseArray(ddShareRes);
                    for (int i = 0; i < shareLists.size(); i++) {
                        JSONObject jsonObject = JSONObject.parseObject(shareLists.getString(i));
                        if (jsonObject.getInteger("position") == 3) {
                            String icon = concatUrl(url, jsonObject.getString("url"), "g" + game.getDdCode(), "share");
                            if (StringUtils.isNotBlank(icon)) {
                                roundGame.setJumpDirect(icon);
                            }
                        }
                    }
                }
            }
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, roundGame.getDdRound());
            roundGame.setRoundName(roundExt.getDdName());
            roundGame.setDdReward(roundExt.getDdReward());
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, RoundGame entity) {
        if (entity.getDdState() == null) {
            entity.setDdState(false);
        }
    }

    @Override
    protected boolean update(String requestParam, RoundGame entity, UpdateWrapper<RoundGame> updateWrapper) {
        if (entity.getDdState() == null) {
            entity.setDdState(false);
        }
        updateWrapper.eq("ddCode", entity.getId());
        return this.update(entity, updateWrapper);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundGame> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            String list = StrUtil.sub(requestParam, 1, -1);
            List<String> idList = Lists.newArrayList(StringUtils.split(list, ","));
            return this.removeByIds(idList);
        }
        return false;
    }

    @Autowired
    public void setGameSetService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setGameSetService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
