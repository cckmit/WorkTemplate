package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdContentMapper;
import com.fish.dao.second.mapper.ConfigAdStrategyMapper;
import com.fish.dao.second.model.ConfigAdStrategy;
import com.fish.dao.second.model.ConfigAdStrategy;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdStrategyService implements BaseService<ConfigAdStrategy> {

    @Autowired
    ConfigAdStrategyMapper adStrategyMapper;
    @Autowired
    CacheService cacheService;
    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class getClassInfo() { return ConfigAdStrategy.class; }

    @Override
    public boolean removeIf(ConfigAdStrategy configAdContent, JSONObject searchData) { return false; }

    @Override
    public List selectAll(GetParameter parameter) { return this.adStrategyMapper.selectAll(); }

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
        }
        cacheService.updateAllConfigAdStrategys();
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
        }
        cacheService.updateAllConfigAdStrategys();
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param id
     * @return
     */
    public PostResult delete(int id) {
        PostResult result = new PostResult();
        int delete = this.adStrategyMapper.delete(id);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        cacheService.updateAllConfigAdStrategys();
        return result;
    }

    public List<ConfigAdStrategy> selectAllAdStrategy(GetParameter getParameter) {
        {
            List<ConfigAdStrategy> configAdadStrategys = adStrategyMapper.selectAll();
            for (ConfigAdStrategy configAdStrategy : configAdadStrategys) {
                Integer ddId = configAdStrategy.getDdId();
                String ddName = configAdStrategy.getDdName();
                configAdStrategy.setDdName(ddId + "-" + ddName);
            }
            return configAdadStrategys;
        }
    }
}
