package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdStrategyMapper;
import com.fish.dao.second.model.ConfigAdStrategy;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdStrategyService extends CacheService<ConfigAdStrategy> implements BaseService<ConfigAdStrategy> {

    @Autowired
    ConfigAdStrategyMapper adStrategyMapper;

    @Autowired
    BaseConfig baseConfig;

    @Autowired
    ConfigAdStrategyService adStrategyService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class getClassInfo() {
        return ConfigAdStrategy.class;
    }

    @Override
    public boolean removeIf(ConfigAdStrategy configAdContent, JSONObject searchData) {
        return false;
    }

    @Override
    public List selectAll(GetParameter parameter) {
        return this.adStrategyMapper.selectAll();
    }

    /**
     * 新增广告内容
     *
     * @param adStrategy
     * @return
     */
    public PostResult insert(ConfigAdStrategy adStrategy) {
        PostResult result = new PostResult();
        int id = this.adStrategyMapper.insert(adStrategy);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        } else {
            this.updateAllCache(ConfigAdStrategy.class);
            ReadJsonUtil.flushTable("config_ad_strategy", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adStrategy
     * @return
     */
    public PostResult update(ConfigAdStrategy adStrategy) {
        PostResult result = new PostResult();
        int update = this.adStrategyMapper.update(adStrategy);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            this.updateCache(ConfigAdStrategy.class, String.valueOf(adStrategy.getDdId()), adStrategy);
            ReadJsonUtil.flushTable("config_ad_strategy", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param deleteIds
     * @return
     */
    public PostResult delete(String deleteIds) {
        PostResult result = new PostResult();
        int delete = this.adStrategyMapper.delete(deleteIds);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            this.updateAllCache(ConfigAdStrategy.class);
            ReadJsonUtil.flushTable("config_ad_strategy", this.baseConfig.getFlushCache());
        }
        return result;
    }

    public List<ConfigAdStrategy> selectAllAdStrategy(GetParameter getParameter) {
        List<ConfigAdStrategy> configAdStrategies = adStrategyMapper.selectAll();
        for (ConfigAdStrategy configAdStrategy : configAdStrategies) {
            Integer ddId = configAdStrategy.getDdId();
            String ddName = configAdStrategy.getDdName();
            configAdStrategy.setDdName(ddId + "-" + ddName);
        }
        return configAdStrategies;
    }

    @Override
    void updateAllCache(ConcurrentHashMap<String, ConfigAdStrategy> map) {
        List<ConfigAdStrategy> list = this.adStrategyMapper.selectAll();
        list.forEach(adStrategy -> map.put(String.valueOf(adStrategy.getDdId()), adStrategy));
    }

    @Override
    ConfigAdStrategy queryEntity(Class<ConfigAdStrategy> clazz, String key) {
        return this.adStrategyMapper.select(Integer.parseInt(key));
    }

}
