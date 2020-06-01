package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdSpace;
import com.cc.manager.modules.jj.entity.ConfigAdType;
import com.cc.manager.modules.jj.mapper.ConfigAdSpaceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:32
 */
@Service
@DS("jj")
public class ConfigAdSpaceService extends BaseCrudService<ConfigAdSpace, ConfigAdSpaceMapper> {

    private ConfigAdTypeService configAdTypeService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdSpace> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String id = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(id), "ddId", id);
            String adType = queryObject.getString("adType");
            queryWrapper.eq(StringUtils.isNotBlank(adType), "ddAdType", adType);
        }
    }

    @Override
    protected void rebuildSelectedEntity(ConfigAdSpace entity) {
        if (entity.getAdType() != 0) {
            entity.setAdTypeName(this.configAdTypeService.getCacheValue(ConfigAdType.class, String.valueOf(entity.getAdType())));
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdSpace> deleteWrapper) {
        return false;
    }

    /**
     * 切换运营状态
     *
     * @param requestParam 请求参数
     * @return 切换结果
     */
    public PostResult statusSwitch(String requestParam) {
        PostResult postResult = new PostResult();
        try {
            JSONObject requestObject = JSONObject.parseObject(requestParam);
            UpdateWrapper<ConfigAdSpace> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("ddAllowedOperation", requestObject.getBoolean("status"));
            updateWrapper.eq("ddId", requestObject.getString("id"));
            if (!this.update(updateWrapper)) {
                postResult.setCode(2);
                postResult.setMsg("操作失败！");
            }
        } catch (RuntimeException e) {
            postResult.setCode(2);
            postResult.setMsg("操作失败！");
        }
        return postResult;
    }

    @Autowired
    public void setConfigAdTypeService(ConfigAdTypeService configAdTypeService) {
        this.configAdTypeService = configAdTypeService;
    }

}
