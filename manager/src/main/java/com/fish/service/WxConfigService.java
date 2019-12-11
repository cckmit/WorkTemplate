package com.fish.service;

import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WxConfigService implements BaseService<WxConfig> {

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    AppConfigMapper appConfigMapper;
    @Autowired
    AppConfig appConfig;

    @Override
    //查询展示所有wxconfig信息
    public List<WxConfig> selectAll(GetParameter parameter) {
        return wxConfigMapper.selectAll();
    }

    //新增展示所有产品信息
    public int insert(WxConfig record) {
        //新增判断是否产品名称重复
//        WxConfig wxConfig = wxConfigMapper.selectByProductName(record.getProductName());
////        if (!StringUtils.isEmpty(wxConfig)) {
////            return 4;
////        }
        int insert = 0;
        appConfig.setDdappid(record.getDdappid());
        appConfig.setDdname(record.getProductName());
        appConfig.setDdprogram(record.getProgramType());
        appConfig.setDdtime(new Timestamp(new Date().getTime()));
        try {
            insert = appConfigMapper.insert(appConfig);
            System.out.println("appConfig插入数据" + insert);
        } catch (Exception e) {
            e.printStackTrace();
            //新增判断AppId重复
            insert = 3;
        }
        record.setCreateTime(new Timestamp(new Date().getTime()));
        int insert1 = 0;
        try {
            //新增产品信息
            insert1 = wxConfigMapper.insert(record);
            System.out.println("wx插入数据 :" + insert1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insert1;
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(WxConfig record) {
        //产品名称去重
        int insert = 0;
//        List<WxConfig> wxConfigs = wxConfigMapper.selectAll();
//        List<WxConfig> wxConfigOthers =new ArrayList<>();
//        for (WxConfig wxConfigSingle : wxConfigs) {
//            if (!record.getDdappid().equals(wxConfigSingle.getDdappid()))
//                wxConfigOthers.add(wxConfigSingle);
//        }
//        for (WxConfig wxConfigOther : wxConfigOthers) {
//            if (record.getProductName().equals(wxConfigOther.getProductName()))
//                return 4;
//        }
        appConfig.setDdappid(record.getDdappid());
        appConfig.setDdname(record.getProductName());
        appConfig.setDdprogram(record.getProgramType());
        appConfig.setDdtime(new Timestamp(new Date().getTime()));
        try {
            insert = appConfigMapper.updateByPrimaryKeySelective(appConfig);
            System.out.println("appConfig插入数据" + insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wxConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<WxConfig> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(WxConfig wxConfig, Map<String, String> searchData) {


        if (existValueFalse(searchData.get("appId"), wxConfig.getDdappid())) {
            return true;
        }

//        if (existValueFalse(searchData.get("belong"), wxConfig.getBelongCompany())) {
//            return true;
//        }
//
//        if (existValueFalse(searchData.get("clear"), wxConfig.getClearCompany())) {
//            return true;
//        }
        return existValueFalse(searchData.get("productsName"), wxConfig.getProductName());
    }
}
