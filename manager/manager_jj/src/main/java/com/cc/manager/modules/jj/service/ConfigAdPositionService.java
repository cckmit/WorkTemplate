package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdPosition;
import com.cc.manager.modules.jj.entity.ConfigAdType;
import com.cc.manager.modules.jj.mapper.ConfigAdPositionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:34
 */
@Service
@DS("jj")
public class ConfigAdPositionService extends BaseCrudService<ConfigAdPosition, ConfigAdPositionMapper> {

    private ConfigAdTypeService configAdTypeService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdPosition> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String id = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(id), "ddId", id);
        }
    }

    @Override
    protected void rebuildSelectedEntity(ConfigAdPosition entity) {
        if (StringUtils.isNotBlank(entity.getAdTypes())) {
            String[] adTypeArray = StringUtils.split(entity.getAdTypes(), ",");
            String adTypeNames = null;
            for (String adType : adTypeArray) {
                String adTypeName = this.configAdTypeService.getCacheValue(ConfigAdType.class, adType);
                if (StringUtils.isBlank(adTypeNames)) {
                    adTypeNames = adTypeName;
                } else {
                    adTypeNames += ", " + adTypeName;
                }
            }
            entity.setAdTypeNames(adTypeNames);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, ConfigAdPosition entity) {
        entity.setAllowedOperation(StringUtils.equals("1", entity.getAllowedOperation()) ? "1" : "0");
        entity.setShowWxAd(StringUtils.equals("1", entity.getShowWxAd()) ? "1" : "0");
        entity.setShowWxReVideoAd(StringUtils.equals("1", entity.getShowWxReVideoAd()) ? "1" : "0");
    }

    @Override
    protected boolean update(String requestParam, ConfigAdPosition entity, UpdateWrapper<ConfigAdPosition> updateWrapper) {
        entity.setAllowedOperation(StringUtils.equals("1", entity.getAllowedOperation()) ? "1" : "0");
        entity.setShowWxAd(StringUtils.equals("1", entity.getShowWxAd()) ? "1" : "0");
        entity.setShowWxReVideoAd(StringUtils.equals("1", entity.getShowWxReVideoAd()) ? "1" : "0");
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdPosition> deleteWrapper) {
        return false;
    }

    /**
     * 表格内数据状态切换
     *
     * @param switchObject 切换对象
     */
    public PostResult statusSwitch(JSONObject switchObject) {
        PostResult postResult = new PostResult();
        try {
            ConfigAdPosition configAdPosition = new ConfigAdPosition();
            configAdPosition.setId(switchObject.getInteger("id"));
            String switchColumn = switchObject.getString("switchColumn");
            String switchValue = Boolean.parseBoolean(switchObject.getString("status")) ? "1" : "0";
            if (StringUtils.equals("allowedOperation", switchColumn)) {
                configAdPosition.setAllowedOperation(switchValue);
            } else if (StringUtils.equals("showWxAd", switchColumn)) {
                configAdPosition.setShowWxAd(switchValue);
            } else if (StringUtils.equals("showWxReVideoAd", switchColumn)) {
                configAdPosition.setShowWxReVideoAd(switchValue);
            }
            if (!this.updateById(configAdPosition)) {
                postResult.setCode(2);
                postResult.setMsg("切换状态失败：更新数据库失败！");
            }
        } catch (Exception e) {
            postResult.setCode(2);
            postResult.setMsg("切换状态失败：数据处理异常！");
        }
        return postResult;
    }

    @Autowired
    public void setConfigAdTypeService(ConfigAdTypeService configAdTypeService) {
        this.configAdTypeService = configAdTypeService;
    }

}
