package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdSpaceMapper;
import com.fish.dao.second.model.ConfigAdContent;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdSpaceService implements BaseService<ConfigAdSpace> {

    @Autowired
    ConfigAdSpaceMapper adSpaceMapper;

    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class getClassInfo() {
        return ConfigAdSpace.class;
    }

    @Override
    public boolean removeIf(ConfigAdSpace configAdSpace, JSONObject searchData) {
        return false;
    }

    @Override
    public List selectAll(GetParameter parameter) {
        List<ConfigAdSpace> configAdSpaces = adSpaceMapper.selectAll();
        for (ConfigAdSpace configAdSpace : configAdSpaces) {
            String ddAdContents = configAdSpace.getDdAdContents();

            String[] splitContents = ddAdContents.split(",");
            ArrayList<String> adContents = new ArrayList<>();
            if (splitContents.length == 1) {
                ConfigAdContent configAdContent = cacheService.getConfigAdContents(Integer.parseInt(splitContents[0]));
                if (configAdContent != null) {
                    configAdSpace.setDdAdContents(configAdContent.getDdId() + "-" + configAdContent.getDdTargetAppName());
                }
            } else {
                for (int i = 0; i < splitContents.length; i++) {
                    ConfigAdContent configAdContent = cacheService.getConfigAdContents(Integer.valueOf(splitContents[i]));
                    if (configAdContent != null) {
                        adContents.add(configAdContent.getDdId() + "-" + configAdContent.getDdTargetAppName());
                    }
                }
                if (adContents.size() > 0) {
                    String adContent = adContents.toString();
                    configAdSpace.setDdAdContents(adContent.substring(1, adContent.length() - 1));
                }

            }
        }
        return configAdSpaces;
    }

    /**
     * 新增广告内容
     *
     * @param adSpace
     * @return
     */
    public PostResult insert(ConfigAdSpace adSpace) {
        PostResult result = new PostResult();
        int id = this.adSpaceMapper.insert(adSpace);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        }
        cacheService.updateAllConfigAdSpaces();
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adSpace
     * @return
     */
    public PostResult update(ConfigAdSpace adSpace) {
        PostResult result = new PostResult();
        int update = this.adSpaceMapper.update(adSpace);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        cacheService.updateAllConfigAdSpaces();
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
        int delete = this.adSpaceMapper.delete(id);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        cacheService.updateAllConfigAdSpaces();
        return result;
    }


    /**
     * select组件数据
     *
     * @param getParameter
     * @return
     */
    public List<ConfigAdSpace> selectAllSpace(GetParameter getParameter) {
        List<ConfigAdSpace> configAdSpaces = this.adSpaceMapper.selectAll();

        for (ConfigAdSpace configAdSpace : configAdSpaces) {
            Integer ddId = configAdSpace.getDdId();
            String ddName = configAdSpace.getDdName();
            configAdSpace.setDdName(ddId + "-" + ddName);
        }
        return configAdSpaces;
    }
}
