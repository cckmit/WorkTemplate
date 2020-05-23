package com.cc.manager.modules.jj.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.GoodsValueExt;
import com.cc.manager.modules.jj.mapper.GoodsValueExtMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class GoodsValueExtService extends BaseCrudService<GoodsValueExt, GoodsValueExtMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<GoodsValueExt> queryWrapper) {

        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String money = queryObject.getString("money");
            queryWrapper.eq(StringUtils.isNotBlank(money), "ddPrice", money);
            String type = queryObject.getString("type");
            queryWrapper.like(StringUtils.isNotBlank(type), "ddGoodsType", type);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<GoodsValueExt> entityList) {
        for (GoodsValueExt goodsValue : entityList) {
            String goodsType = goodsValue.getDdGoodsType();
            String costType = goodsValue.getDdCostType();
            Integer ddValue = goodsValue.getDdValue();
            if ("coin".equals(costType)) {
                BigDecimal ddPrice = goodsValue.getDdPrice();
                goodsValue.setDdCostType(ddPrice.intValue() + "金币");
            } else {
                BigDecimal ddPrice = goodsValue.getDdPrice();
                goodsValue.setDdCostType(ddPrice.intValue() + "元");
            }
            if ("coin".equals(goodsType)) {
                goodsValue.setCostDesc("购买" + ddValue.toString() + "金币");
                goodsValue.setGainDesc(ddValue.toString() + "金币");
                goodsValue.setCoinNumber(goodsValue.getDdValue().toString());
            }
            if ("recharge".equals(goodsType)) {
                int cashNumber = ddValue / 100;
                goodsValue.setCostDesc("提现" + cashNumber + "元");
                goodsValue.setGainDesc(cashNumber + "元");
                goodsValue.setCashNumber(String.valueOf(cashNumber));
            }
            if ("head".equals(goodsType)) {
                goodsValue.setCostDesc("购买" + ddValue.toString() + "号头像");
                goodsValue.setGainDesc(ddValue.toString() + "号头像");
                goodsValue.setHeadNumber(goodsValue.getDdValue().toString());
            }
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, GoodsValueExt entity) {
        dealSubmitData(entity);
    }


    @Override
    protected boolean update(String requestParam, GoodsValueExt entity, UpdateWrapper<GoodsValueExt> updateWrapper) {
        dealSubmitData(entity);
        return this.updateById(entity);
    }

    /**
     * 处理页面提交的计费点数据
     *
     * @param entity entity
     */
    private void dealSubmitData(GoodsValueExt entity) {
        if (entity.getDdState() == null) {
            entity.setDdState(false);
        }
        if (entity.getDdIOS() == null) {
            entity.setDdIOS(false);
        }
        //解析不同商品的类型
        if ("recharge".equals(entity.getDdGoodsType()) || "coin".equals(entity.getDdGoodsType())) {
            entity.setDdCostType("rmb");
            entity.setDdName(entity.getDdPrice() + "元");
        } else {
            entity.setDdCostType("coin");
            entity.setDdName("hf" + entity.getHeadNumber());
        }
        if (StringUtils.isNotBlank(entity.getCoinNumber())) {
            //购买金币
            String coinNumber = entity.getCoinNumber();
            entity.setDdValue(Integer.valueOf(coinNumber));
            entity.setDdPrice(entity.getDdPrice());
            entity.setDdDesc("购买" + coinNumber + "金币");
            entity.setDdFirst(true);
        } else if (StringUtils.isNotBlank(entity.getHeadNumber())) {
            //购买头像框
            String headNumber = entity.getHeadNumber();
            entity.setDdValue(Integer.valueOf(headNumber));
            entity.setDdPrice(entity.getDdPrice());
            entity.setDdDesc(entity.getDdPrice() + "金币");
            entity.setDdFirst(false);
        } else if (StringUtils.isNotBlank(entity.getCashNumber())) {
            //提现
            String cashNumber = entity.getCashNumber();
            entity.setDdValue(Integer.parseInt(cashNumber) * 100);
            entity.setDdPrice(entity.getDdPrice());
            entity.setDdDesc("提现" + cashNumber + "元");
            entity.setDdFirst(false);
        } else {
            entity.setDdValue(0);
            entity.setDdPrice(new BigDecimal(0));
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<GoodsValueExt> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            String list = StrUtil.sub(requestParam, 1, -1);
            List<String> idList = Lists.newArrayList(StringUtils.split(list, ","));
            return this.removeByIds(idList);
        }
        return false;
    }

}
