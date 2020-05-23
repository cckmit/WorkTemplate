package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.WxGroupHistory;
import com.cc.manager.modules.jj.mapper.WxGroupHistoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class WxGroupHistoryService extends BaseCrudService<WxGroupHistory, WxGroupHistoryMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxGroupHistory> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            String wxGroupName = queryObject.getString("wxGroupName");
            String wxGroupManager = queryObject.getString("wxGroupManager");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(updateTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            queryWrapper.like(StringUtils.isNotBlank(wxGroupName), "wxGroupName", wxGroupName);
            queryWrapper.like(StringUtils.isNotBlank(wxGroupManager), "wxGroupManager", wxGroupManager);
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxGroupHistory> deleteWrapper) {
        return false;
    }
}
