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
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class RechargeRecordService extends BaseStatsService<Recharge, RechargeMapper> {

    private RechargeMapper rechargeMapper;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Recharge> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Recharge> entityList, StatsListResult statsListResult) {
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        try {
            String start = "", end = "" , uid  = "", userName= "",productName= "",ddStatus="";
            if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
                JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
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
            List<Recharge> recharges = this.rechargeMapper.selectAllChargeRecord(start, end);
            List<Recharge> newRecharges = new ArrayList<>();
            for (Recharge recharge : recharges) {
                if(StringUtils.isNotBlank(uid)){
                    if(!recharge.getDdUid().contains(uid)){
                        continue;
                    }
                }
                if(StringUtils.isNotBlank(userName)){
                    if(!recharge.getUserName().contains(userName)){
                        continue;
                    }
                }
                if(StringUtils.isNotBlank(productName)){
                    if(!recharge.getProductName().contains(productName)){
                        continue;
                    }
                }
                if(StringUtils.isNotBlank(ddStatus)){
                    if(recharge.getDdStatus() != (Integer.parseInt(ddStatus))){
                        continue;
                    }
                }
                newRecharges.add(recharge);
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(newRecharges)));
            statsListResult.setCount(recharges.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    @Autowired
    public void setRechargeMapper(RechargeMapper rechargeMapper) {
        this.rechargeMapper = rechargeMapper;
    }

}
