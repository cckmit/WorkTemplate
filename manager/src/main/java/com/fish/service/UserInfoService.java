package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        List<UserAllInfo> userInfos;
        String ddName = search.getString("ddname");
        String uid = search.getString("uid");
        String ddOpenId = search.getString("ddoid");
        userInfos = userInfoMapper.selectAllUserInfo(ddName, uid, ddOpenId);

        //已提现金额用户的提现金额赋值
        Map<String, BigDecimal> userRechargedMap = queryUserRecharged();
        for (String userId : userRechargedMap.keySet()) {
            for (UserAllInfo userAllInfo : userInfos) {
                if (userAllInfo.getDduid().equals(userId)) {
                    userAllInfo.setCashOut(userRechargedMap.get(userId).intValue());
                }
            }
        }
        return userInfos;
    }

    /**
     * 已提现金额查询
     *
     * @return
     */
    private Map<String, BigDecimal> queryUserRecharged() {
        Map<String, BigDecimal> rechargeMap = new HashMap<>(10);
        List<Recharge> userRecharged = rechargeMapper.selectAllUserRecharged();
        for (Recharge recharge : userRecharged) {
            rechargeMap.put(recharge.getDduid(), recharge.getDdrmb());
        }
        return rechargeMap;
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
        String registerTime = searchData.getString("registertime");
        if (StringUtils.isNotBlank(registerTime)) {
            Date[] parse = XwhTool.parseDate(registerTime);
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
            after.setTime(Objects.requireNonNull(endTime));
            after.add(Calendar.DATE, 1);
            before.setTime(startTime);
            before.add(Calendar.DATE, -1);
            //搜索结束时间在数据时间之前
            boolean before1 = userAllInfo.getDdregistertime().before(after.getTime());
            //搜索开始时间在数据时间之后
            boolean after2 = userAllInfo.getDdregistertime().after(before.getTime());
            //符合条件返回false展示数据
            return !(before1 && after2);
        }
        return (existValueFalse(searchData.getString("appSelect"), userAllInfo.getDdappid()));
    }

}
