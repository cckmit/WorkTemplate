package com.fish.service;

import com.fish.dao.second.mapper.GoodValueInfoMapper;
import com.fish.dao.second.mapper.RecordVirtualMapper;
import com.fish.dao.second.model.GoodValueInfo;
import com.fish.dao.second.model.RecordVirtual;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BasicUserPayInfoService implements BaseService<RecordVirtual>
{
    @Autowired
    RecordVirtualMapper virtualMapper;
    @Autowired
    GoodValueInfoMapper goodValueInfoMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() == null)
        {
            parameter.setSort("inserttime");
            parameter.setOrder("desc");
        }
    }

    @Override
    public Class<RecordVirtual> getClassInfo()
    {
        return RecordVirtual.class;
    }

    @Override
    public boolean removeIf(RecordVirtual recordVirtual, Map<String, String> searchData)
    {
        if (existTimeFalse(recordVirtual.getInserttime(), searchData.get("times")))
            return true;
        if (existValueFalse(searchData.get("userId"), recordVirtual.getUserid()))
            return true;
        if (existValueFalse(searchData.get("nickName"), recordVirtual.getNickname()))
            return true;
        return existValueFalse(searchData.get("goodsId"), recordVirtual.getGoodsid());
    }

    @Override
    public List<RecordVirtual> selectAll(GetParameter parameter)
    {
        return virtualMapper.selectAll();
    }

    public GoodValueInfo selectByGoodsId(Integer goodsId)
    {
        GoodValueInfo goodValueInfo = goodValueInfoMapper.selectByGoodsId(goodsId);
        return goodValueInfo;
    }

    public List<GoodValueInfo> selectAllGoods()
    {
        return goodValueInfoMapper.selectAll();
    }
}
