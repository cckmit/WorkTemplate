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
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class WxGroupHistoryService extends BaseCrudService<WxGroupHistory, WxGroupHistoryMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxGroupHistory> queryWrapper) {

        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("times");
            String wxGroupName = queryData.getString("wxGroupName");
            String wxGroupManager = queryData.getString("wxGroupManager");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(updateTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            if (StringUtils.isNotBlank(times)) {
                queryWrapper.like("wxGroupName", wxGroupName);
            }
            if (StringUtils.isNotBlank(times)) {
                queryWrapper.like("wxGroupManager", wxGroupManager);
            }
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxGroupHistory> deleteWrapper) {
        return false;
    }
}
