package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.OrdersMapper;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowPayStatistic;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    /**
     * 查询付费信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ShowPayStatistic> selectAll(GetParameter parameter) {
        List<ShowPayStatistic> payStatistics = new ArrayList<>();
        JSONObject search = getSearchData(parameter.getSearchData());
        //判断是否存在查询条件
        if (search == null || (search.getString("start").isEmpty() && search.getString("end").isEmpty())) {
            //无查询条件查询
            payStatistics = noSearchQuery();
        } else {
            payStatistics = searchQuery(search);
        }
        return payStatistics;
    }

    /**
     * 条件查询
     *
     * @param search
     * @return
     */
    private List<ShowPayStatistic> searchQuery(JSONObject search) {
        List<ShowPayStatistic> payStatistics = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = search.getString("start");
        String end = search.getString("end");
        //根据搜索时间查询含有订单支付成功日期
        List<Orders> pays = ordersMapper.selectDdTransByTime(start, end);
        for (Orders pay : pays) {
            Date ddtrans = pay.getDdtrans();
            String time = format.format(ddtrans);
            //根据日期查询当前AppId
            List<Orders> appIds = ordersMapper.selectAppIdByTime(time);
            for (Orders appId : appIds) {
                ShowPayStatistic payStatistic = new ShowPayStatistic();
                String ddappid = appId.getDdappid();
                //根据日期，AppId查询全部支付金额
                Orders payMoneys = ordersMapper.selectPayByTimeAndAppId(time, ddappid);
                BigDecimal countPrice = payMoneys.getDdprice();
                Integer ddgid = payMoneys.getDdgid();
                int up = countPrice.intValue() / ddgid;
                //支付总金额
                payStatistic.setPayMoney(countPrice.intValue());
                //支付用户数
                payStatistic.setPayUsers(ddgid);
                payStatistic.setPayUp(up + "");
                payStatistic.setDdappid(ddappid);
                WxConfig wxConfig = cacheService.getWxConfig(ddappid);
                String productName = wxConfig.getProductName();
                Integer programType = wxConfig.getProgramType();
                payStatistic.setProductName(productName);
                payStatistic.setProductType(programType);
                payStatistic.setDdtrans(ddtrans);
                //根据条件过滤不符合数据
                String ddAppId = search.getString("productName");
                String payState = search.getString("payState");
                if (ddAppId != null && ddAppId.length() > 0) {
                    if (payState != null && payState.length() > 0) {
                        if (ddAppId.equals(ddappid) && (Integer.valueOf(payState).equals(programType))) {
                            payStatistics.add(payStatistic);
                        }
                    } else {
                        if (ddAppId.equals(ddappid)) {
                            payStatistics.add(payStatistic);
                        }
                    }
                } else {
                    if (payState != null && payState.length() > 0) {
                        if (Integer.valueOf(payState).equals(programType)) {
                            payStatistics.add(payStatistic);
                        }
                    } else {
                        payStatistics.add(payStatistic);
                    }
                }
            }
        }
        return payStatistics;
    }

    /**
     * 无条件查询
     *
     * @return
     */
    private List<ShowPayStatistic> noSearchQuery() {
        List<ShowPayStatistic> payStatistics = new ArrayList<>();
        //当前时间
        String cur = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        //根据日期查询当前AppId
        List<Orders> pays = ordersMapper.selectAppIdByTime(cur);
        for (Orders orders : pays) {
            ShowPayStatistic payStatistic = new ShowPayStatistic();
            String ddappid = orders.getDdappid();
            //根据日期，AppId查询全部支付金额
            Orders payMoneys = ordersMapper.selectPayByTimeAndAppId(cur, ddappid);
            BigDecimal countPrice = payMoneys.getDdprice();
            Integer ddgid = payMoneys.getDdgid();
            int up = countPrice.intValue() / ddgid;
            //支付总金额
            payStatistic.setPayMoney(countPrice.intValue());
            //支付用户数
            payStatistic.setPayUsers(ddgid);
            payStatistic.setPayUp(up + "");
            payStatistic.setDdappid(ddappid);
            WxConfig wxConfig = cacheService.getWxConfig(ddappid);
            String productName = wxConfig.getProductName();
            Integer programType = wxConfig.getProgramType();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(cur);
            } catch (ParseException e) {
                LOGGER.error("PayStatisticService时间转换失败" + ", 详细信息:{}", e.getMessage());
            }
            payStatistic.setProductName(productName);
            payStatistic.setProductType(programType);
            payStatistic.setDdtrans(date);
            payStatistics.add(payStatistic);
        }
        return payStatistics;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
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
