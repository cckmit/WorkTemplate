package com.cc.manager.modules.jj.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.GameSet;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.mapper.GameSetMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-21
 */
@Service
@DS("jj")
public class GameSetService extends BaseCrudService<GameSet, GameSetMapper> {

    private GamesService gamesService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<GameSet> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String gameId = queryObject.getString("gameId");
            queryWrapper.like(StringUtils.isNotBlank(gameId), "ddCode", gameId);
            String gameName = queryObject.getString("gameName");
            queryWrapper.like(StringUtils.isNotBlank(gameName), "ddName", gameName);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<GameSet> entityList) {
        for (GameSet gameSet : entityList) {
            String ddContent = gameSet.getDdContent512a();
            String gameSetContent = ddContent.replace("#", ",");
            gameSet.setGameSetContent(gameSetContent);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, GameSet entity) {

        gameSetDeal(entity);

    }

    @Override
    protected boolean update(String requestParam, GameSet entity, UpdateWrapper<GameSet> updateWrapper) {
        gameSetDeal(entity);
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<GameSet> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            return this.removeByIds(idList);
        }
        return false;
    }

    /**
     * 处理游戏合集数据
     *
     * @param entity entity
     */
    private void gameSetDeal(GameSet entity) {
        String gameSetContent = entity.getGameSetContent();
        StringBuilder ddDesc = new StringBuilder();
        String content512a = gameSetContent.replace(",", "#");
        entity.setDdContent512a(content512a);
        String[] gameSetSelect = gameSetContent.split(",");
        List<String> games = new ArrayList<>(Arrays.asList(gameSetSelect));
        for (String gameCode : games) {
            Games gameEntity = this.gamesService.getCacheEntity(Games.class, gameCode);
            String gameName = gameEntity.getDdName();
            if (ddDesc.length() > 0) {
                ddDesc.append(",");
            }
            ddDesc.append(gameName);
        }
        entity.setDdDesc(ddDesc.toString());
    }

    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

}
