package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.Recharge;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 提现审核 Service
 * RechargedService
 *
 * @author
 * @date
 */
@Service
public class RechargedService implements BaseService<Recharge> {
    @Autowired
    RechargeMapper rechargeMapper;
    @Autowired
    UserAppMapper userAppMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserValueMapper userValueMapper;
    @Autowired
    CacheService cacheService;
    @Autowired
    AllCostMapper allCostMapper;

    @Override
    public List<Recharge> selectAll(GetParameter parameter) {
        long current = System.currentTimeMillis();
        List<Recharge> recharges;
        JSONObject search = getSearchData(parameter.getSearchData());
        System.out.println("查询,耗时:" + (System.currentTimeMillis() - current) + "ms");
        if (search == null || search.getString("times").isEmpty()) {
            recharges = rechargeMapper.selectAllCharge();
        } else {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            recharges = rechargeMapper.selectChargedByTime(format.format(parse[0]), format.format(parse[1]));
        }
        System.out.println("查询提现结束,耗时:" + (System.currentTimeMillis() - current) + "ms");
        return recharges;
    }

    //新增信息
    public int insert(Recharge record) {
        return rechargeMapper.insert(record);
    }

    //更新信息
    public int updateByPrimaryKeySelective(Recharge record) {
        return rechargeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("ddtimes");
        parameter.setOrder("desc");
    }

    @Override
    public Class<Recharge> getClassInfo() {
        return Recharge.class;
    }

    @Override
    public boolean removeIf(Recharge recharge, JSONObject searchData) {
        if (existValueFalse(searchData.getString("uid"), recharge.getDduid())) {
            return true;
        }
        if (existValueFalse(searchData.getString("userName"), recharge.getUserName())) {
            return true;
        }
        if (existValueFalse(searchData.getString("productName"), recharge.getDdappid())) {
            return true;
        }
        return (existValueFalse(searchData.getString("ddStatus"), recharge.getDdstatus()));
    }


}
