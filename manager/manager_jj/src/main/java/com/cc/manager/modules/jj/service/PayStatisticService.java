package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.mapper.OrdersMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("jj")
public class PayStatisticService extends BaseStatsService<Orders, OrdersMapper> {

    private OrdersService ordersService;
    private JjAndFcAppConfigService jjAndFcAppConfigService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Orders> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Orders> entityList, StatsListResult statsListResult) {
        List<Orders> newEntityList = new ArrayList<>();
        // 获取街机和FC的全部app信息
        LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
        String appId = statsListParam.getQueryObject().getString("appId");
        String productType = statsListParam.getQueryObject().getString("productType");
        for (Orders order : entityList) {
            if (StringUtils.isNotBlank(appId)) {
                if (!StringUtils.equals(appId, order.getDdAppId())) {
                    continue;
                }
            }
            JSONObject appObject = getAllAppMap.get(order.getDdAppId());
            if (appObject != null) {
                // 设置data产品信息
                order.setProductName(appObject.getString("name"));
                order.setProductType(Integer.parseInt(appObject.getString("programType")));
            }
            if (StringUtils.isNotBlank(productType)) {
                if (Integer.parseInt(productType) != order.getProductType()) {
                    continue;
                }
            }
            //处理人均付费数据
            order.setPayUp(order.getDdPrice().divide(new BigDecimal(order.getPayUsers()), 2, ROUND_HALF_UP).toString());
            newEntityList.add(order);
        }
        entityList.clear();
        entityList.addAll(newEntityList);
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空，并进行初始化
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }

        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");

        try {
            //查询实时付费数据
            List<Orders> entityList = ordersService.queryBuyStatistic(beginDate, endDate);
            if (Objects.nonNull(entityList)) {
                this.rebuildStatsListResult(statsListParam, entityList, statsListResult);
                statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
                statsListResult.setCount(entityList.size());
            }
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
