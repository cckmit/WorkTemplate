package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.BuyPayMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class BuyPayService extends BaseCrudService<BuyPay, BuyPayMapper> {

    private WxConfigService wxConfigService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<BuyPay> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "buy_app_id", appId);
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(buy_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<BuyPay> deleteWrapper) {
        return false;
    }

    /**
     * 根据起止时间查询购买数据
     *
     * @param appId     appId
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 数据列表
     */
    public List<BuyPay> list(String appId, String beginDate, String endDate) {
        QueryWrapper<BuyPay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(appId), "buy_app_id", appId).between("buy_date", beginDate, endDate);
        return this.list(queryWrapper);
    }

    /**
     * 导入买量数据
     *
     * @param record record
     */
    public int insertExcel(JSONObject record) {
        String context = record.getString("context");
        System.out.println("context :" + context);
        context = context.substring(1, context.length() - 1);
        try {
            JSONArray param = new JSONArray(Collections.singletonList(context));
            List<BuyPay> lists = new ArrayList<>();
            for (Object o : param) {
                String singleData = o.toString();
                String singleString = singleData.substring(1, singleData.length() - 1);
                String[] split = singleString.split("], ");
                for (int j = 0; j < split.length; j++) {
                    if (j != 0) {
                        String single = split[j].substring(1);
                        String[] singleSplit = single.split(",");
                        Map<String, String> mapSingle = new HashMap<>();
                        BuyPay buyPay = new BuyPay();
                        for (int x = 0; x < singleSplit.length; x++) {
                            switch (x) {
                                case 0:
                                    mapSingle.put("buyDate", singleSplit[x].trim());
                                    break;
                                case 1:
                                    mapSingle.put("buyProductName", singleSplit[x].trim());
                                    break;
                                case 2:
                                    mapSingle.put("buyCost", singleSplit[x].trim());
                                    break;
                                case 3:
                                    mapSingle.put("buyClickNumber", singleSplit[x].trim());
                                    break;
                                case 4:
                                    mapSingle.put("buyClickPrice", singleSplit[x].trim());
                                    break;
                                case 5:
                                    mapSingle.put("appId", singleSplit[x].trim());
                                    break;
                                default:
                                    break;
                            }
                        }
                        String buyDate = mapSingle.get("buyDate");
                        String productName = mapSingle.get("buyProductName");
                        String buyCost = mapSingle.get("buyCost");
                        String buyClickNumber = mapSingle.get("buyClickNumber");
                        String buyClickPrice = mapSingle.get("buyClickPrice");
                        String appId = mapSingle.get("appId");
                        if (StringUtils.isNotBlank(appId)) {
                            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
                            if (wxConfig != null) {
                                String ddName = wxConfig.getProductName();
                                if (productName.equals(ddName)) {
                                    buyPay.setBuyProductName(productName);
                                } else {
                                    buyPay.setBuyProductName(ddName);
                                }
                            }
                            buyPay.setBuyDate(buyDate);
                            buyPay.setBuyAppId(appId);
                            buyPay.setBuyCost(new BigDecimal(buyCost));
                            buyPay.setBuyClickNumber(Integer.parseInt(buyClickNumber));
                            buyPay.setBuyClickPrice(new BigDecimal(buyClickPrice));
                            buyPay.setInsertTime(LocalDateTime.now());
                            lists.add(buyPay);
                        }
                    }
                }
                this.saveOrUpdateBatch(lists);
            }
        } catch (JSONException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 2;
        }
        return 1;
    }

    @Override
    public boolean saveOrUpdate(BuyPay entity) {
        String buyDate = entity.getBuyDate();
        String buyAppId = entity.getBuyAppId();
        QueryWrapper<BuyPay> buyPayQueryWrapper = new QueryWrapper<>();
        buyPayQueryWrapper.eq("buy_date", buyDate).eq("buy_app_id", buyAppId);
        BuyPay tableContent = this.getOne(buyPayQueryWrapper);
        //数据存在更新，不存在则新增
        if (tableContent != null) {
            UpdateWrapper<BuyPay> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("buy_date", buyDate).eq("buy_app_id", buyAppId);
            return this.update(entity, updateWrapper);
        } else {
            return this.save(entity);
        }
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<BuyPay> entityList, int batchSize) {
        try {
            for (BuyPay buyPay : entityList) {
                this.saveOrUpdate(buyPay);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    /**
     * 通过时间段查询买量汇总数据
     *
     * @param beginTime beginTime
     * @param endTime   endTime
     * @param type      type
     * @return List
     */
    public List<BuyPay> queryByPayCollectByDate(String beginTime, String endTime, String type) {
        QueryWrapper<BuyPay> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("buy_date", beginTime, endTime).groupBy("buy_date").orderByDesc("buy_date");

        List<String> selectList = Lists.newArrayList("buy_date as buyDate", " SUM(buy_cost) as buyCost");
        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        return this.list(queryWrapper);

    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

}
