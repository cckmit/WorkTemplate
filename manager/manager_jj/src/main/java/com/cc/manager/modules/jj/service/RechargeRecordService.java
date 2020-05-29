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
            // 初始化查询的起止日期
            this.updateBeginAndEndDate(statsListParam);

            String start = statsListParam.getQueryObject().getString("beginDate");
            String end = statsListParam.getQueryObject().getString("endDate");

            String uid = statsListParam.getQueryObject().getString("uid");
            String userName = statsListParam.getQueryObject().getString("appId");
            String productName = statsListParam.getQueryObject().getString("productName");
            String ddStatus = statsListParam.getQueryObject().getString("ddStatus");


            if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
                JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
                String times = queryObject.getString("times");
                if (StringUtils.isNotBlank(times)) {
                    String[] timeRangeArray = StringUtils.split(times, "~");
                    start = timeRangeArray[0].trim();
                    end = timeRangeArray[1].trim();
                }

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

    /**
     * 初始化查询起止时间
     *
     * @param statsListParam 请求参数
     */
    private void updateBeginAndEndDate(StatsListParam statsListParam) {
        String beginDate = null, endDate = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginDate = timeRangeArray[0].trim();
            endDate = timeRangeArray[1].trim();
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = "2020-04-10";//DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2))
            endDate = "2020-04-10";  //DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setRechargeMapper(RechargeMapper rechargeMapper) {
        this.rechargeMapper = rechargeMapper;
    }

}
