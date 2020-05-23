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
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.mapper.AllCostMapper;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 提现审核
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class RechargeAuditService extends BaseCrudService<Recharge, RechargeMapper> {

    private RechargeMapper rechargeMapper;
    private  AllCostMapper allCostMapper;

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
        String uid="",userName="",productName ="",ddStatus="";
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                start = timeRangeArray[0].trim();
                end = timeRangeArray[1].trim();
            }
             uid = queryObject.getString("uid");
             userName = queryObject.getString("userName");
             productName = queryObject.getString("productName");
             ddStatus = queryObject.getString("ddStatus");
        }
        List<Recharge> recharges = this.rechargeMapper.selectAll(start, end);
        List<Recharge> rechargeList = new ArrayList<>();
        for (Recharge recharge : recharges) {
            String ddUid = recharge.getDdUid();
            if(StringUtils.isNotBlank(uid)){
                if(!uid.equals(ddUid)){
                    continue;
                }
            }
            if(StringUtils.isNotBlank(userName)){
                if(!userName.equals(recharge.getUserName())){
                    continue;
                }
            }
            if(StringUtils.isNotBlank(productName)){
                if(!productName.equals(recharge.getDdAppId())){
                    continue;
                }
            }
            if(StringUtils.isNotBlank(ddStatus)){
                if(!ddStatus.equals(recharge.getDdStatus())){
                    continue;
                }
            }
            LocalDateTime times = recharge.getDdTimes();
            String ddTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(times);
            AllCost allCost = allCostMapper.selectCurrentCoin(ddTime);
            if (allCost != null) {
                Long rmbCurrent = allCost.getDdCurrent();
                //剩余金额
                recharge.setRemainAmount(rmbCurrent.intValue() / 100);
            } else {
                recharge.setRemainAmount(0);
            }
            int cashOutCurrent = rechargeMapper.selectCashOut(ddUid, ddTime);
            //已提现金额
            recharge.setRmbOut(new BigDecimal(cashOutCurrent));
            Integer programType = recharge.getProgramType();
            if (programType == 1 || programType == 2) {
                rechargeList.add(recharge);
            }
        }

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Recharge> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setRechargeMapper(RechargeMapper rechargeMapper) {
        this.rechargeMapper = rechargeMapper;
    }
    @Autowired
    public void setAllCostMapper(AllCostMapper allCostMapper) {
        this.allCostMapper = allCostMapper;
    }

}
