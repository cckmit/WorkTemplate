package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdPositionMapper;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.dao.second.model.ConfigAdSpace;
import com.fish.dao.second.model.ConfigAdType;
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
public class ConfigAdPositionService implements BaseService<ConfigAdPosition>
{

    @Autowired
    ConfigAdPositionMapper adPositionMapper;
    @Autowired
    CacheService cacheService;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
    }

    @Override
    public Class getClassInfo()
    {
        return ConfigAdPositionService.class;
    }

    @Override
    public boolean removeIf(ConfigAdPosition configAdPosition, JSONObject searchData)
    {
        return false;
    }

    @Override
    public List selectAll(GetParameter parameter)
    {
        List<ConfigAdPosition> list = this.adPositionMapper.selectAll();

        for (ConfigAdPosition configAdPosition : list)
        {
            String ddAdTypes = configAdPosition.getDdAdTypes();
            String[] splitTypes = ddAdTypes.split(",");
            ArrayList<String> adTypes = new ArrayList<>();
            if (splitTypes.length == 1)
            {
                ConfigAdType configAdType = cacheService.getConfigAdType(Integer.valueOf(splitTypes[0]));
                configAdPosition.setDdAdTypes(configAdType.getDdName());
            } else
            {
                for (int i = 0; i < splitTypes.length; i++)
                {
                    ConfigAdType configAdType = cacheService.getConfigAdType(Integer.valueOf(splitTypes[i]));
                    adTypes.add(configAdType.getDdName());
                }
                String adType = adTypes.toString();
                configAdPosition.setDdAdTypes(adType.substring(1, adType.length() - 1));
            }
            String ddAdSpaces = configAdPosition.getDdAdSpaces();
            String[] splitSpaces = ddAdSpaces.split(",");
            ArrayList<String> adSpaces = new ArrayList<>();
            if (splitSpaces.length == 1)
            {
                ConfigAdSpace configAdSpace = cacheService.getConfigAdSpaces(Integer.valueOf(splitTypes[0]));
                if (configAdSpace != null)
                {
                    configAdPosition.setDdAdSpaces(configAdSpace.getDdName());
                }
            } else
            {
                for (int i = 0; i < splitTypes.length; i++)
                {
                    ConfigAdSpace configAdSpace = cacheService.getConfigAdSpaces(Integer.parseInt(splitTypes[i]));
                    if (configAdSpace != null)
                    {
                        configAdPosition.setDdAdSpaces(configAdSpace.getDdName());
                        adSpaces.add(configAdSpace.getDdName());
                    }

                }
                if (adSpaces.size() > 0)
                {
                    String adSpace = adSpaces.toString();
                    configAdPosition.setDdAdSpaces(adSpace.substring(1, adSpace.length() - 1));
                }

            }
        }
        return list;
    }

    /**
     * 新增广告位置
     *
     * @param adPosition
     * @return
     */
    public PostResult insert(ConfigAdPosition adPosition)
    {
        PostResult result = new PostResult();
        int id = this.adPositionMapper.insert(adPosition);
        if (id <= 0)
        {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        }
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adContent
     * @return
     */
    public PostResult update(ConfigAdPosition adContent)
    {
        PostResult result = new PostResult();
        int update = this.adPositionMapper.update(adContent);
        if (update <= 0)
        {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param id
     * @return
     */
    public PostResult delete(int id)
    {
        PostResult result = new PostResult();
        int delete = this.adPositionMapper.delete(id);
        if (delete <= 0)
        {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        }
        return result;
    }

}
