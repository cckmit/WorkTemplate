package com.cc.manager.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.sys.entity.DataDict;
import com.cc.manager.modules.sys.mapper.DataDictMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 21:49
 */
@Service
public class DataDictService extends BaseCrudService<DataDict, DataDictMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<DataDict> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String dataType = queryObject.getString("dataType");
            queryWrapper.like(StringUtils.isNotBlank(dataType), "data_type", dataType);
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<DataDict> deleteWrapper) {
        return false;
    }

}
