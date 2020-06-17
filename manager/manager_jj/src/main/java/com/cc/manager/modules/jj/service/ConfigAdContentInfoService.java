package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdContentInfo;
import com.cc.manager.modules.jj.mapper.ConfigAdContentInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-06-16
 */
@Service
@DS("jj")
public class ConfigAdContentInfoService extends BaseCrudService<ConfigAdContentInfo, ConfigAdContentInfoMapper> {


    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdContentInfo> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String targetAppId = queryObject.getString("appId");
            queryWrapper.eq(StringUtils.isNotBlank(targetAppId), "ddTargetAppId", targetAppId);
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdContentInfo> deleteWrapper) {
        return false;
    }

}
