package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.AllCost;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserInfo;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RechargedService implements BaseService<Recharge>
{
    //提现情况
    private static final Logger LOG = LoggerFactory.getLogger(RechargedService.class);
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
    //查询提现成功订单
    public List<Recharge> selectAll(GetParameter parameter)
    {
        List<Recharge> recharges;
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null || search.getString("times").isEmpty())
        {
            recharges = rechargeMapper.selectAllCharged();
        } else
        {
            Date[] parse = XwhTool.parseDate(search.getString("times"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            recharges = rechargeMapper.selectChargedByTime(format.format(parse[0]), format.format(parse[1]));
        }
        for (Recharge recharge : recharges)
        {
            String dduid = recharge.getDduid();

            Date times = recharge.getDdtimes();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ddTime = sdf.format(times);
            String sql =String.format(" SELECT * FROM all_cost WHERE ddTime ='%s' and ddType = 'rmb'",ddTime);
            //String sql = " SELECT * FROM all_cost WHERE ddTime ='" + ddTime + "' ";
            AllCost allCost = allCostMapper.selectCurrentCoin(sql);
            if (allCost != null)
            {
                Long rmbCurrent = allCost.getDdcurrent();
                //剩余金额
                recharge.setRemainAmount(rmbCurrent.intValue() / 100);
            } else
            {
                recharge.setRemainAmount(0);
            }
            String reChargeSql =String.format("SELECT COUNT(ddRmb) FROM recharge WHERE ddStatus = 200 AND ddUid = '%s' AND ddTimes <= '%s' ",dduid,ddTime);
            //String reChargeSql = "SELECT COUNT(ddRmb) FROM recharge WHERE ddStatus = 200 AND ddUid = '" + dduid + "' AND ddTimes <= '" + ddTime + "' ";
            int cashOutCurrent = rechargeMapper.selectCashOut(reChargeSql);
//            String cashSql = " SELECT * FROM all_cost WHERE ddCostType ='recharge' AND ddUid = '"+dduid+"'AND ddTime <='" + ddTime + "' ";
//            List<AllCost> cashCosts = allCostMapper.selectCurrentCash(cashSql);
//            for (AllCost cashCost : cashCosts)
//            {
//                Integer ddvalue = cashCost.getDdvalue();
//                cashOut += ddvalue;
//            }
            //已提现金额
            recharge.setDdrmbed(new BigDecimal(cashOutCurrent));
            String ddAppId = recharge.getDdappid();
            com.fish.dao.second.model.WxConfig wxConfig = cacheService.getWxConfig(ddAppId);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                Integer programType = wxConfig.getProgramType();
                recharge.setProductName(productName);
                recharge.setProgramType(programType);
            }
            UserInfo userInfo = userInfoMapper.selectByDdUid(dduid);
            if (userInfo != null)
            {
                String userName = userInfo.getDdname();
                recharge.setUserName(userName);
            }

        }
        return recharges;
    }

    //新增信息
    public int insert(Recharge record)
    {
        return rechargeMapper.insert(record);
    }

    //更新信息
    public int updateByPrimaryKeySelective(Recharge record)
    {
        return rechargeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("ddtimes");
        parameter.setOrder("desc");
    }

    @Override
    public Class<Recharge> getClassInfo()
    {
        return Recharge.class;
    }

    @Override
    public boolean removeIf(Recharge recharge, JSONObject searchData)
    {
        if (existValueFalse(searchData.getString("uid"), recharge.getDduid()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("userName"), recharge.getUserName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("productName"), recharge.getDdappid()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("ddStatus"), recharge.getDdstatus()));
    }


}
