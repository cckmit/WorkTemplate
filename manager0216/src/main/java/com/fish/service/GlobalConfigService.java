package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 全局配置 商品
 * GlobalConfigService
 *
 * @author
 * @date
 */
@Service
public class GlobalConfigService implements BaseService<GoodsValue> {

    @Autowired
    GoodsValueMapper goodsValueMapper;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询所有商品信息
     *
     * @param parameter
     * @return
     */
    @Override
    public List<GoodsValue> selectAll(GetParameter parameter) {
        List<GoodsValue> goodsValues = goodsValueMapper.selectAll();
        for (GoodsValue goodsValue : goodsValues) {
            String goodsType = goodsValue.getDdGoodsType();
            String costType = goodsValue.getDdCostType();
            Integer ddvalue = goodsValue.getDdValue();
            if ("coin".equals(costType)) {
                BigDecimal DdPrice = goodsValue.getDdPrice();
                goodsValue.setDdCostType(DdPrice.intValue() + "金币");
            } else {
                BigDecimal DdPrice = goodsValue.getDdPrice();
                goodsValue.setDdCostType(DdPrice.intValue() + "元");
            }
            if ("coin".equals(goodsType)) {
                goodsValue.setCostDesc("购买" + ddvalue.toString() + "金币");
                goodsValue.setGainDesc(ddvalue.toString() + "金币");
                goodsValue.setCoinNumber(goodsValue.getDdValue().toString());
            }
            if ("recharge".equals(goodsType)) {
                int cashNumber = ddvalue / 100;
                goodsValue.setCostDesc("提现" + cashNumber + "元");
                goodsValue.setGainDesc(cashNumber + "元");
                goodsValue.setCashNumber(String.valueOf(cashNumber));
            }
            if ("head".equals(goodsType)) {
                goodsValue.setCostDesc("购买" + ddvalue.toString() + "号头像");
                goodsValue.setGainDesc(ddvalue.toString() + "号头像");
                goodsValue.setHeadNumber(goodsValue.getDdValue().toString());
            }
        }
        return goodsValues;
    }

    /**
     * 新增 goods_value_ext
     *
     * @param record
     * @return
     */
    public int insert(GoodsValue record) {
        record.setInsertTime(new Timestamp(System.currentTimeMillis()));
        String goodsType = record.getDdGoodsType();
        //解析不同商品的类型
        if ("recharge".equals(goodsType) || "coin".equals(goodsType)) {
            record.setDdCostType("rmb");
            record.setDdName(record.getDdPrice() + "元");
        } else {
            record.setDdCostType("coin");
            record.setDdName("hf" + record.getHeadNumber());
        }
        if (StringUtils.isNotBlank(record.getCoinNumber())) {
            //购买金币
            String coinNumber = record.getCoinNumber();
            record.setDdValue(Integer.valueOf(coinNumber));
            record.setDdPrice(record.getDdPrice());
            record.setDdDesc("购买" + coinNumber + "金币");
            record.setDdFirst(true);
        } else if (StringUtils.isNotBlank(record.getHeadNumber())) {
            //购买头像框
            String headNumber = record.getHeadNumber();
            record.setDdValue(Integer.valueOf(headNumber));
            record.setDdPrice(record.getDdPrice());
            record.setDdDesc(record.getDdPrice() + "金币");
            record.setDdFirst(false);
        } else if (StringUtils.isNotBlank(record.getCashNumber())) {
            //提现
            String cashNumber = record.getCashNumber();
            record.setDdValue(Integer.parseInt(cashNumber) * 100);
            record.setDdPrice(record.getDdPrice());
            record.setDdDesc("提现" + cashNumber + "元");
            record.setDdFirst(false);
        } else {
            record.setDdValue(0);
            record.setDdPrice(new BigDecimal(0));
        }
        return goodsValueMapper.insertSelective(record);
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(GoodsValue record) {
        record.setInsertTime(new Timestamp(System.currentTimeMillis()));
        String goodsType = record.getDdGoodsType();
        //解析不同商品的类型
        if ("recharge".equals(goodsType) || "coin".equals(goodsType)) {
            record.setDdCostType("rmb");
            record.setDdName(record.getDdPrice() + "元");
        } else {
            record.setDdCostType("coin");
            record.setDdName("hf" + record.getHeadNumber());
        }
        if (StringUtils.isNotBlank(record.getCoinNumber())) {
            String coinNumber = record.getCoinNumber();
            record.setDdValue(Integer.valueOf(coinNumber));
            record.setDdPrice(record.getDdPrice());
            record.setDdDesc("购买" + coinNumber + "金币");
        } else if (StringUtils.isNotBlank(record.getHeadNumber())) {
            String headNumber = record.getHeadNumber();
            record.setDdValue(Integer.valueOf(headNumber));
            record.setDdPrice(record.getDdPrice());
            record.setDdDesc(record.getDdPrice() + "金币");
        } else if (StringUtils.isNotBlank(record.getCashNumber())) {
            String cashNumber = record.getCashNumber();
            record.setDdValue(Integer.parseInt(cashNumber) * 100);
            record.setDdPrice(new BigDecimal(record.getDdPrice().toString()));
            record.setDdDesc("提现" + cashNumber + "元");
        }
        return goodsValueMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    public PostResult deleteSelective(JSONObject jsonObject) {
        PostResult result = new PostResult();
        String ddId = jsonObject.getString("deleteIds");
        int delete = this.goodsValueMapper.deleteByPrimaryKey(Integer.parseInt(ddId));
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            //刷新业务表结构
            ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushCache());
            ReadJsonUtil.flushTable("goods_value_ext", baseConfig.getFlushPublicCache());
        }
        return result;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<GoodsValue> getClassInfo() {
        return GoodsValue.class;
    }

    @Override
    public boolean removeIf(GoodsValue goodsValue, JSONObject searchData) {
        if (existValueFalse(searchData.getString("money"), goodsValue.getDdPrice().intValue())) {
            return true;
        }
        return (existValueFalse(searchData.getString("type"), goodsValue.getDdGoodsType()));
    }
}
