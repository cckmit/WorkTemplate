package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowPayStatistic;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 实时付费信息
 * Service
 *
 * @author
 * @date
 */
@Service
public class PayStatisticService implements BaseService<ShowPayStatistic> {

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    WxConfigService wxConfigService;

    /**
     * 查询付费信息
     *
     * @param parameter parameter
     * @return 付费信息
     */
    @Override
    public List<ShowPayStatistic> selectAll(GetParameter parameter) {
        List<ShowPayStatistic> showPayStatistics = new ArrayList<>();
        Map<String, WxConfig> wxConfigMap = this.wxConfigService.getAll(WxConfig.class);
        JSONObject search = getSearchData(parameter.getSearchData());
        String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String ddappid = "";
        String payState = "";
        // 判断前台传的时间
        if (search != null) {
            if (!StringUtils.isBlank(search.getString("start"))) {
                start = search.getString("start");
            }
            if (!StringUtils.isBlank(search.getString("end"))) {
                end = search.getString("end");
            }
            ddappid = search.getString("productName");
            payState = search.getString("payState");
        }
        // 查询订单表
        List<Orders> orders = ordersMapper.queryBuyStatis(start, end, ddappid, payState);
        // 循环赋值
        orders.forEach(order -> {
            ShowPayStatistic payStatistic = new ShowPayStatistic();
            // 付费金额
            BigDecimal countPrice = order.getDdprice();
            payStatistic.setPayMoney(countPrice.doubleValue());
            double up = countPrice.doubleValue() / order.getPayUsers();
            DecimalFormat df = new DecimalFormat("#.00");
            // 人均付费
            payStatistic.setPayUp(df.format(up) + "");
            // 时间
            payStatistic.setDdtrans(order.getDdtrans());
            // 付费人数
            payStatistic.setPayUsers(order.getPayUsers());
            // 名称
            payStatistic.setProductName(wxConfigMap.get(order.getDdappid()).getProductName());
            // 类型
            payStatistic.setProductType(wxConfigMap.get(order.getDdappid()).getProgramType());
            showPayStatistics.add(payStatistic);
        });
        return showPayStatistics;
    }


    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setSort("productName");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ShowPayStatistic> getClassInfo() {
        return ShowPayStatistic.class;
    }

    @Override
    public boolean removeIf(ShowPayStatistic showPayStatistic, JSONObject searchData) {
        return false;
    }

}
