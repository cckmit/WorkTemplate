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
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.OrdersMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    private WxConfigService wxConfigService;
    private GoodsValueExtService goodsValueExtService;
    private OrdersMapper ordersMapper;

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        String start = "2020-03-24";
        String end = "2020-03-24";
        //  String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                start = timeRangeArray[0].trim();
                end = timeRangeArray[1].trim();
            }
        }
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        try {
            // 初始化查询wrapper
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

            List<Orders> entityList = ordersMapper.queryBuyStatistic(start, end);
            if (Objects.nonNull(entityList)) {
                JSONObject totalRow = this.rebuildStatsListResult(statsListParam, entityList, statsListResult);
                statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
                statsListResult.setTotalRow(totalRow);
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

        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddTrans)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String appId = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
            String productType = queryObject.getString("productType");
            queryWrapper.eq(StringUtils.isNotBlank(productType), "ddRound", productType);
        }
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Orders> entityList, StatsListResult statsListResult) {
        List<Orders> newEntityList = new ArrayList<>();
        JSONObject queryData = null;
        String productNameSelect = null;
        String productTypeSelect = null;
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            queryData = JSONObject.parseObject(statsListParam.getQueryData());
        }
        if (queryData != null) {
            productNameSelect = queryData.getString("productName");
            productTypeSelect = queryData.getString("productType");
        }
        for (Orders order : entityList) {
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, order.getDdAppId());
            //产品名称
            String productName = wxConfig.getProductName();
            Integer programType = wxConfig.getProgramType();
            order.setProductName(productName);
            order.setProductType(programType);
            if (StringUtils.isNotBlank(productNameSelect)) {
                if (!productNameSelect.equals(order.getProductName())) {
                    continue;
                }
            }
            if (StringUtils.isNotBlank(productTypeSelect)) {
                if (!productTypeSelect.equals(order.getProductType().toString())) {
                    continue;
                }
            }

            order.setPayUp(order.getDdPrice().divide(new BigDecimal(order.getPayUsers()), 2, ROUND_HALF_UP).toString());
            newEntityList.add(order);
        }
        entityList.clear();
        entityList.addAll(newEntityList);
        return null;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setGoodsValueExtService(GoodsValueExtService goodsValueExtService) {
        this.goodsValueExtService = goodsValueExtService;
    }

    @Autowired
    public void setOrdersMapper(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

}
