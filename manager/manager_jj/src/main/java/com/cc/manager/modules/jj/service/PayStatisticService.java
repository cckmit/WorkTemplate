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
import com.cc.manager.modules.jj.entity.GoodsValueExt;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.WxConfig;
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
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("jj")
public class PayStatisticService extends BaseCrudService<Orders, OrdersMapper> {
    private WxConfigService wxConfigService;
    private GoodsValueExtService goodsValueExtService;
    private OrdersMapper ordersMapper;
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Orders> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {

        }
    }

    /**
     * 分页查询
     *
     * @param crudPageParam 分页请求参数
     */
    @Override
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        String start ="2020-03-24";
        String end = "2020-03-24";
        //  String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                start = timeRangeArray[0].trim();
                end = timeRangeArray[1].trim();
            }
        }
        CrudPageResult pageResult = new CrudPageResult();
        try {
            Page<Orders> page = new Page<>(crudPageParam.getPage(), crudPageParam.getLimit());
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
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
            //this.updateGetPageWrapper(crudPageParam, queryWrapper);
            List<Orders> entityList = ordersMapper.queryBuyStatistic(start, end);
            if (Objects.nonNull(entityList)) {
                this.rebuildSelectedList(crudPageParam, entityList);
                pageResult.setCount(entityList.size());
                pageResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
            }
        } catch (Exception e) {
            pageResult.setCode(1);
            pageResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return pageResult;
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<Orders> entityList) {
        List<Orders> newEntityList =new ArrayList<>();
        JSONObject queryData = null;
        String productNameSelect = null;
        String productTypeSelect = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
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
           if(StringUtils.isNotBlank(productNameSelect)){
               if(!productNameSelect.equals(order.getProductName())){
                   continue;
               }
           }
            if(StringUtils.isNotBlank(productTypeSelect)){
                if(!productTypeSelect.equals(order.getProductType().toString())){
                    continue;
                }
            }

            order.setPayUp(order.getDdPrice().divide(new BigDecimal(order.getPayUsers()),2,ROUND_HALF_UP).toString());
            newEntityList.add(order);
        }
        entityList.clear();
        entityList.addAll(newEntityList);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Orders> deleteWrapper) {
        return false;
    }


    @Override
    protected void updateInsertEntity(String requestParam, Orders entity) {

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
