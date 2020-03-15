package com.fish.service;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.AllCost;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.LocaleData;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * 用户信息
 *
 * @author
 * @date
 */
@Service
public class UserInfoService implements BaseService<UserAllInfo> {
    @Autowired
    AllCostMapper allCostMapper;
    @Autowired
    UserValueMapper uerValueMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    CacheService cacheService;

    @Autowired
    RechargeMapper rechargeMapper;

    @Override
    public List<UserAllInfo> selectAll(GetParameter parameter) {
        JSONObject search = getSearchData(parameter.getSearchData());
        if (search == null) {
            return new Vector<>();
        }
        List<UserAllInfo> userInfos = null;
        String ddname = search.getString("ddname");
        String uid = search.getString("uid");
        String ddOpenID = search.getString("ddoid");
        userInfos = userInfoMapper.selectByRegister(ddname, uid, ddOpenID);
        for (UserAllInfo userInfo : userInfos) {
            String ddappid = userInfo.getDdappid();
            String dduid = userInfo.getDduid();

            WxConfig wxConfig = cacheService.getWxConfig(ddappid);
            if (wxConfig != null) {
                String productName = wxConfig.getProductName();
                userInfo.setProductName(productName);
            }
            String cashRmb = "select SUM(ddRmb) from recharge WHERE ddStatus = 200 AND ddUid ='" + dduid + "'";
            BigDecimal decimal = rechargeMapper.selectRecharged(cashRmb);
            if (decimal != null) {
                userInfo.setCashOut(decimal.intValue());
            } else {
                userInfo.setCashOut(0);
            }
            String coinSql = "select * from all_cost where ddUid ='" + dduid + "' and ddType ='coin' ORDER BY id DESC LIMIT 1";
            AllCost coinCost = allCostMapper.selectCurrentCoin(coinSql);

            String rmbSql = "select * from all_cost where ddUid ='" + dduid + "' and ddType ='rmb' ORDER BY id DESC LIMIT 1";
            AllCost rmbCost = allCostMapper.selectCurrentCoin(rmbSql);
            if (coinCost != null) {
                Long coinCurrent = coinCost.getDdcurrent();
                userInfo.setDdcoincount(coinCurrent.intValue());
            } else {
                userInfo.setDdcoincount(0);
            }
            if (rmbCost != null) {
                //剩余可提现金额
                Long rmbCurrent = rmbCost.getDdcurrent();
                userInfo.setRemainMoney(rmbCurrent.intValue() / 100);
            } else {
                userInfo.setRemainMoney(0);
            }
        }
        return userInfos;
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<UserAllInfo> getClassInfo() {
        return UserAllInfo.class;
    }

    @Override
    public boolean removeIf(UserAllInfo userAllInfo, JSONObject searchData) {
        String registertime = searchData.getString("registertime");
        if (StringUtils.isNotBlank(registertime)) {
            Date[] parse = XwhTool.parseDate(registertime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String start = format.format(parse[0]);
            String end = format.format(parse[1]);
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = format.parse(start);
                endTime = format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar before = Calendar.getInstance();
            Calendar after = Calendar.getInstance();
            after.setTime(endTime);
            after.add(Calendar.DATE, 1);
            before.setTime(startTime);
            before.add(Calendar.DATE, -1);
            boolean before1 = userAllInfo.getDdregistertime().before(after.getTime());
            boolean after2 = userAllInfo.getDdregistertime().after(before.getTime());
            return !(before1 && after2);
        }
        return false;
    }

}
