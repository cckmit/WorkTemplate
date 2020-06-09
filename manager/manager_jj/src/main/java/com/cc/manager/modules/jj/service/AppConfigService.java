package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.AppConfig;
import com.cc.manager.modules.jj.entity.GameSet;
import com.cc.manager.modules.jj.mapper.AppConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class AppConfigService extends BaseCrudService<AppConfig, AppConfigMapper> {

    private GameSetService gameSetService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<AppConfig> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
            String appPlatform = queryObject.getString("appPlatform");
            queryWrapper.eq(StringUtils.isNotBlank(appPlatform), "ddAppPlatform", appPlatform);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<AppConfig> entityList) {
        for (AppConfig appConfig : entityList) {
            if (appConfig.getDdCheckCode() != null) {
                GameSet gameSet = this.gameSetService.getCacheEntity(GameSet.class, appConfig.getDdCheckCode().toString());
                if (gameSet != null) {
                    appConfig.setCheckCodeName(gameSet.getDdName());
                }
            }
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<AppConfig> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            return this.removeByIds(idList);
        }
        return false;
    }

    protected boolean delete(List<String> requestParam) {
        return this.removeByIds(requestParam);
    }

    @Override
    protected void updateInsertEntity(String requestParam, AppConfig entity) {
    }

    @Autowired
    public void setGameSetService(GameSetService gameSetService) {
        this.gameSetService = gameSetService;
    }

}
