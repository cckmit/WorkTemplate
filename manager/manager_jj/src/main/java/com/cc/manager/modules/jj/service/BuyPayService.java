package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.mapper.BuyPayMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class BuyPayService extends BaseCrudService<BuyPay, BuyPayMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<BuyPay> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(buy_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, BuyPay entity) {

    }

    @Override
    protected boolean update(String requestParam, BuyPay entity, UpdateWrapper<BuyPay> updateWrapper) {
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<BuyPay> deleteWrapper) {
        return false;
    }
}
