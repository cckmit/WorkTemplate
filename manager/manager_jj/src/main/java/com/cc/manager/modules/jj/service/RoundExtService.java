package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.mapper.RoundExtMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author cf
 * @since 2020-05-11
 */
@Service
public class RoundExtService extends BaseCrudService<RoundExt, RoundExtMapper> {
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundExt> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {

            String times = queryData.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(insertTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundExt> deleteWrapper) {
        return false;
    }


    @Override
    protected void updateInsertEntity(String requestParam, RoundExt entity) {

    }

}
