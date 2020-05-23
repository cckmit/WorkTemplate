package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdType;
import com.cc.manager.modules.jj.mapper.ConfigAdTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:30
 */
@Service
@DS("jj")
public class ConfigAdTypeService extends BaseCrudService<ConfigAdType, ConfigAdTypeMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdType> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())){
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String name = queryObject.getString("name");
            queryWrapper.like(StringUtils.isNotBlank(name),"ddName",name);
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdType> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            return this.removeByIds(idList);
        }
        return false;
    }

}
