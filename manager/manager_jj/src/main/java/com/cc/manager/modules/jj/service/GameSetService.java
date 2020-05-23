package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.GameSet;
import com.cc.manager.modules.jj.mapper.GameSetMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class GameSetService extends BaseCrudService<GameSet, GameSetMapper> {
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<GameSet> queryWrapper) {
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

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<GameSet> entityList) {

    }

    @Override
    protected void updateInsertEntity(String requestParam, GameSet entity) {

    }

    @Override
    protected boolean update(String requestParam, GameSet entity, UpdateWrapper<GameSet> updateWrapper) {
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<GameSet> deleteWrapper) {
        return false;
    }

}
