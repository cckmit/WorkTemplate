package com.fish.service;

import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.RechargeMapper;
import com.fish.dao.second.mapper.UserAppMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserApp;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RechargeService implements BaseService<Recharge> {

    @Autowired
    RechargeMapper rechargeMapper;
    @Autowired
    UserAppMapper userAppMapper;

    @Override
    //查询展示appconfig信息
    public List<Recharge> selectAll(GetParameter parameter) {
        List<Recharge> recharges = rechargeMapper.selectAll();
        for (Recharge recharge : recharges) {
            String dduid = recharge.getDduid();
            UserApp userApp = userAppMapper.selectByPrimaryKey(dduid);
            if (userApp != null) {
                String ddoid = userApp.getDdoid();
                recharge.setDdopenid(ddoid);
            }
        }
        return recharges;
    }

    //新增appconfig信息
    public int insert(Recharge record) {

        return rechargeMapper.insert(record);
    }

    //更新appconfig信息
    public int updateByPrimaryKeySelective(Recharge record) {

        return rechargeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<Recharge> getClassInfo() {
        return Recharge.class;
    }

    @Override
    public boolean removeIf(Recharge appConfig, Map<String, String> searchData) {

//        if (existValueFalse(searchData.get("gameName"), appConfig.getDdname())) {
//            return true;
//        }
        return true;
    }
}
