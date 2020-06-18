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
    protected boolean update(String requestParam, ConfigAdPosition entity, UpdateWrapper<ConfigAdPosition> updateWrapper) {
        entity.setAdTypes(StringUtils.strip(entity.getAdTypes(), "[]"));
        // 更新数据
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdPosition> deleteWrapper) {
        return false;
    }

    /**
     * 表格内数据状态切换
     *
     * @param requestParam 请求参数
     */
    public PostResult statusSwitch(String requestParam) {
        PostResult postResult = new PostResult();
        try {
            JSONObject requestObject = JSONObject.parseObject(requestParam);
            String switchColumn = requestObject.getString("switchColumn");
            boolean status = requestObject.getBoolean("status");
            UpdateWrapper<ConfigAdPosition> updateWrapper = new UpdateWrapper<>();
            if (StringUtils.equals("allowedOperation", switchColumn)) {
                updateWrapper.set("ddAllowedOperation", status);
            } else if (StringUtils.equals("showWxAd", switchColumn)) {
                updateWrapper.set("ddShowWxAd", status);
            } else if (StringUtils.equals("showWxReVideoAd", switchColumn)) {
                updateWrapper.set("ddShowWxReVideoAd", status);
            } else {
                updateWrapper = null;
            }
            if (updateWrapper != null) {
                updateWrapper.eq("ddId", requestObject.getInteger("id"));
                if (!this.update(updateWrapper)) {
                    postResult.setCode(2);
                    postResult.setMsg("操作失败！");
                }
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
