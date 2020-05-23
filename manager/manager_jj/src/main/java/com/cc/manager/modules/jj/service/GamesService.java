package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.XwhTool;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.mapper.GamesMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class GamesService extends BaseCrudService<Games, GamesMapper> {
    private GamesMapper gamesMapper;
    private JjConfig jjConfig;
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Games> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String gameId = queryData.getString("gameId");
            String gameName = queryData.getString("gameName");
            if (StringUtils.isNotBlank(gameId)) {
                queryWrapper.like("ddCode", gameId);
            }
            if (StringUtils.isNotBlank(gameName)) {
                queryWrapper.like("ddName", gameName);
            }
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, Games entity) {

    }

    @Override
    protected boolean update(String requestParam, Games entity, UpdateWrapper<Games> updateWrapper) {
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Games> deleteWrapper) {
        return false;
    }

    public int flushGamesResources(JSONObject parameter) {
        int updateGames = 0;
        JSONArray array = parameter.getJSONArray("gameList");
        for (int i = 0; i < array.size(); i++) {
            int gameCode = array.getInteger(i);
            Games game = gamesMapper.selectByPrimaryKey(gameCode);
            try {
                if (game != null) {
                    String resPath = this.jjConfig.getReadRes();
                    String share = XwhTool.readFileString(resPath.concat("g" + gameCode).concat("/share/readme.json"));
                    if (share != null) {
                        game.setDdShareRes(share);
                        updateGames += gamesMapper.updateByPrimaryKeySelective(game);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return updateGames;
    }
    @Autowired
    public void setGamesMapper(GamesMapper gamesMapper) {
        this.gamesMapper = gamesMapper;
    }
    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
