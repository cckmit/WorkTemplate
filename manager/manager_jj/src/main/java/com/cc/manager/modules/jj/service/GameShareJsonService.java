package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.mapper.GamesMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class GameShareJsonService extends BaseCrudService<Games, GamesMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Games> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String gameId = queryObject.getString("gameId");
            queryWrapper.like(StringUtils.isNotBlank(gameId), "ddCode", gameId);
            String gameName = queryObject.getString("gameName");
            queryWrapper.like(StringUtils.isNotBlank(gameName), "ddName", gameName);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, Games entity) {

    }

    @Override
    protected boolean update(String requestParam, Games entity, UpdateWrapper<Games> updateWrapper) {
        updateWrapper.eq("id", entity.getId());
        return this.update(entity, updateWrapper);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Games> deleteWrapper) {
        return false;
    }

}
