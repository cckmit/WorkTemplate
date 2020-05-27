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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private OrdersMapper ordersMapper;

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String start = time;
        String end = time;
        String ddAppId = "";
        String productType = "";
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            String times = queryObject.getString("times");
            ddAppId = queryObject.getString("id");
            productType = queryObject.getString("productType");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                start = timeRangeArray[0].trim();
                end = timeRangeArray[1].trim();
            }
        }
        StatsListResult statsListResult = new StatsListResult();
        try {
            //查询实时付费数据
            List<Orders> entityList = ordersMapper.queryBuyStatistic(start, end, ddAppId, productType);
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

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Orders> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Orders> entityList, StatsListResult statsListResult) {
        List<Orders> newEntityList = new ArrayList<>();
        for (Orders order : entityList) {
            //处理人均付费数据
            order.setPayUp(order.getDdPrice().divide(new BigDecimal(order.getPayUsers()), 2, ROUND_HALF_UP).toString());
            newEntityList.add(order);
        }
        entityList.clear();
        entityList.addAll(newEntityList);
        return null;
    }

    @Autowired
    public void setOrdersMapper(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

}
