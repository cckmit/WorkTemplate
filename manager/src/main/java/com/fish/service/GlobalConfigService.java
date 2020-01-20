package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.GoodsValueMapper;
import com.fish.dao.second.model.GoodsValue;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GlobalConfigService implements BaseService<GoodsValue>
{

    @Autowired
    GoodsValueMapper goodsValueMapper;

    @Override
    //查询展示所有产品信息
    public List<GoodsValue> selectAll(GetParameter parameter)
    {

        List<GoodsValue> goodsValues = goodsValueMapper.selectAll();
        for (GoodsValue goodsValue : goodsValues)
        {
            String goodsType = goodsValue.getDdgoodstype();
            Integer ddvalue = goodsValue.getDdvalue();
            if ("coin".equals(goodsType))
            {
                goodsValue.setCoinNumber(ddvalue.toString());
            }
            if ("recharge".equals(goodsType))
            {
                int cashNumber =  ddvalue / 100;
                goodsValue.setCashNumber(String.valueOf(cashNumber));
            }
            if ("head".equals(goodsType))
            {
                goodsValue.setHeadNumber(ddvalue.toString());
            }
        }

        return goodsValues;
    }

    //新增展示所有产品信息
    public int insert(GoodsValue record)
    {
        record.setInserttime(new Timestamp(new Date().getTime()));
        String goodsType = record.getDdgoodstype();
        if ("recharge".equals(goodsType) || "coin".equals(goodsType))
        {
            record.setDdcosttype("rmb");
        }
        if (StringUtils.isNotBlank(record.getCoinNumber()))
        {
            String coinNumber = record.getCoinNumber();
            record.setDdvalue(Integer.valueOf(coinNumber));
            record.setDdprice(new BigDecimal(coinNumber));
            record.setDddesc("购买" + coinNumber + "金币");
        } else if (StringUtils.isNotBlank(record.getHeadNumber()))
        {
            String headNumber = record.getHeadNumber();
            record.setDdvalue(Integer.valueOf(headNumber));
            record.setDdprice(new BigDecimal(headNumber));
            record.setDddesc(headNumber + "金币");
        } else if (StringUtils.isNotBlank(record.getCashNumber()))
        {
            String cashNumber = record.getCashNumber();
            record.setDdvalue(Integer.parseInt(cashNumber) * 100);
            record.setDdprice(new BigDecimal(cashNumber));
            record.setDddesc("提现" + cashNumber + "元");
        } else
        {
            record.setDdvalue(0);
            record.setDdprice(new BigDecimal(0));
        }
        return goodsValueMapper.insertSelective(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(GoodsValue record)
    {
        record.setInserttime(new Timestamp(new Date().getTime()));
        String goodsType = record.getDdgoodstype();
        if ("recharge".equals(goodsType) || "coin".equals(goodsType))
        {
            record.setDdcosttype("rmb");
        }
        if (StringUtils.isNotBlank(record.getCoinNumber()))
        {
            String coinNumber = record.getCoinNumber();
            record.setDdvalue(Integer.valueOf(coinNumber));
            record.setDdprice(new BigDecimal(coinNumber));
            record.setDddesc("购买" + coinNumber + "金币");
        } else if (StringUtils.isNotBlank(record.getHeadNumber()))
        {
            String headNumber = record.getHeadNumber();
            record.setDdvalue(Integer.valueOf(headNumber));
            record.setDdprice(new BigDecimal(headNumber));
            record.setDddesc(headNumber + "金币");
        } else if (StringUtils.isNotBlank(record.getCashNumber()))
        {
            String cashNumber = record.getCashNumber();
            record.setDdvalue(Integer.parseInt(cashNumber) * 100);
            record.setDdprice(new BigDecimal(cashNumber));
            record.setDddesc("提现" + cashNumber + "元");
        } else
        {
            record.setDdvalue(0);
            record.setDdprice(new BigDecimal(0));
        }
        return goodsValueMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {

    }

    @Override
    public Class<GoodsValue> getClassInfo()
    {
        return GoodsValue.class;
    }

    @Override
    public boolean removeIf(GoodsValue goodsValue, JSONObject searchData)
    {
        if (existValueFalse(searchData.getString("money"), goodsValue.getDdname()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("type"), goodsValue.getDdgoodstype()));
    }
}
