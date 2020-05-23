package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.SupplementOrder;
import com.cc.manager.modules.jj.mapper.SupplementOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class SupplementOrderService extends BaseCrudService<SupplementOrder, SupplementOrderMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<SupplementOrder> queryWrapper) {

        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(create_time)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String uid = queryObject.getString("uid");
            queryWrapper.like(StringUtils.isNotBlank(uid), "userId", uid);
            String name = queryObject.getString("name");
            queryWrapper.like(StringUtils.isNotBlank(name), "userName", name);
        }

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<SupplementOrder> deleteWrapper) {
        return false;
    }

}
