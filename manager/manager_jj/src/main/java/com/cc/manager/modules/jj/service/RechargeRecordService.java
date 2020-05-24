package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 提现记录查询
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class RechargeRecordService extends BaseCrudService<Recharge, RechargeMapper> {

    private RechargeMapper rechargeMapper;

    /**
     * 分页查询
     *
     * @param crudPageParam 分页请求参数
     */
    @Override
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        CrudPageResult pageResult = new CrudPageResult();
        try {
            Page<Recharge> page = new Page<>(crudPageParam.getPage(), crudPageParam.getLimit());
            QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
            // 更新查询排序条件
            if (StringUtils.isNotBlank(crudPageParam.getOrderBy())) {
                JSONObject orderByObject = JSONObject.parseObject(crudPageParam.getOrderBy());
                orderByObject.forEach((orderByColumn, orderByType) -> {
                    String typeStr = orderByType.toString();
                    if ("ASC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, true, orderByColumn);
                    } else if ("DESC".equalsIgnoreCase(typeStr)) {
                        queryWrapper.orderBy(true, false, orderByColumn);
                    }
                });
            }

            this.updateGetPageWrapper(crudPageParam, queryWrapper);
            IPage<Recharge> entityPages = this.page(page, queryWrapper);
            if (Objects.nonNull(entityPages)) {
                pageResult.setCount(entityPages.getTotal());
                List<Recharge> entityList = entityPages.getRecords();
                this.rebuildSelectedList(crudPageParam, entityList);
                pageResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
            }
        } catch (Exception e) {
            pageResult.setCode(1);
            pageResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return pageResult;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Recharge> queryWrapper) {
        String start = "", end = "";
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                start = timeRangeArray[0].trim();
                end = timeRangeArray[1].trim();
            }
            String uid = queryObject.getString("uid");
            String userName = queryObject.getString("userName");
            String productName = queryObject.getString("productName");
            String ddStatus = queryObject.getString("ddStatus");
        }
        List<Recharge> recharges = this.rechargeMapper.selectAllCharge(start, end);


    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Recharge> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setRechargeMapper(RechargeMapper rechargeMapper) {
        this.rechargeMapper = rechargeMapper;
    }

}
