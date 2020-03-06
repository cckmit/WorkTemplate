package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.SupplementOrderMapper;
import com.fish.dao.primary.model.SupplementOrder;
import com.fish.dao.second.mapper.AllCostMapper;
import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.mapper.UserValueMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AllCost;
import com.fish.dao.second.model.UserInfo;
import com.fish.dao.second.model.UserValue;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class SupplementOrderService implements BaseService<SupplementOrder>
{

    @Autowired
    UserValueMapper userValueMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    SupplementOrderMapper supplementOrderMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;

    @Autowired
    AllCostMapper allCostMapper;

    @Override
    //查询所有补单信息
    public List<SupplementOrder> selectAll(GetParameter parameter)
    {
        return supplementOrderMapper.selectAll();
    }

    //新增补单信息
    public int insert(SupplementOrder record)
    {
        UserValue userValue = new UserValue();
        String userId = record.getUserid();
        Integer coinCount = record.getCoinCount();
        UserValue userValues = userValueMapper.selectByPrimaryKey(userId);
        UserInfo userInfo = userInfoMapper.selectByDdUid(userId);
        Integer orCoin = userValues.getDdcoincount();
        String ddName = userInfo.getDdname();
        //昵称  乱码报错
        if(StringUtils.isNotBlank(ddName)){
            record.setUsername(ddName);
        }
        //手动拼接appName
        String appId = record.getAppid();
        WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
        if (wxConfig != null)
        {
            String productName = wxConfig.getProductName();
            Integer programType = wxConfig.getProgramType();
            if (StringUtils.isNotBlank(productName))
            {
                record.setAppname(productName);
            }
            if(programType !=null){
                record.setProgramType(programType);
            }
        }
        record.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userValue.setDduid(userId);
        userValue.setDdcoincount(coinCount + orCoin);
        userValueMapper.updateByPrimaryKeySelective(userValue);
        String coin = RedisUtils.hget("user-" + userId, "coin");
        coinCount = coinCount + Integer.parseInt(coin);
        RedisUtils.hset("user-" + userId, "coin", String.valueOf(coinCount));
        int insert = supplementOrderMapper.insert(record);
        return insert;
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setOrder("desc");
        parameter.setSort("id");
    }

    @Override
    public Class<SupplementOrder> getClassInfo()
    {
        return SupplementOrder.class;
    }

    @Override
    public boolean removeIf(SupplementOrder record, JSONObject searchData)
    {

        if (existTimeFalse(record.getCreateTime(), searchData.getString("times")))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("name"), record.getUsername()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("uid"), record.getUserid()));
    }


    public SupplementOrder selectCurrentCoin(String uid) {
        SupplementOrder supplementOrder = new SupplementOrder();
        String uidSql = String.format(" SELECT * FROM all_cost WHERE ddUid ='%s' ORDER BY id DESC LIMIT 0,1", uid);
        AllCost allCost = this.allCostMapper.selectCurrentCoin(uidSql);
        UserInfo userInfo = userInfoMapper.selectByDdUid(uid);
        if(allCost !=null ){
            supplementOrder.setCurrentCoin(allCost.getDdcurrent());
            supplementOrder.setAppid(allCost.getDdappid());
        }
        if(userInfo !=null){
            supplementOrder.setUsername(userInfo.getDdname());
        }
        return supplementOrder;
    }
}
