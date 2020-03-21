package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.second.mapper.ConfigSubscribeMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.ConfigSubscribe;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 订阅配置
 * ConfigSubscribeService
 *
 * @author
 * @date
 */
@Service
public class ConfigSubscribeService implements BaseService<ConfigSubscribe> {

    @Autowired
    ConfigSubscribeMapper configSubscribeMapper;
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;

    /**
     * 查询订阅配置
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ConfigSubscribe> selectAll(GetParameter parameter) {
        List<ConfigSubscribe> appConfigs = configSubscribeMapper.selectAll();

        return appConfigs;
    }

    /**
     * 新增订阅配置
     *
     * @param record
     * @return
     */
    public int insert(ConfigSubscribe record) {
        record.setTimes(new Timestamp(System.currentTimeMillis()));
        return configSubscribeMapper.insert(record);
    }

    /**
     * 更新订阅配置
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(ConfigSubscribe record) {
        record.setTimes(new Timestamp(System.currentTimeMillis()));
        return configSubscribeMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 默认排序
     *
     * @param parameter
     * @return
     */
    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class<ConfigSubscribe> getClassInfo() {
        return ConfigSubscribe.class;
    }

    /**
     * 筛选
     *
     * @param searchData
     * @return
     */
    @Override
    public boolean removeIf(ConfigSubscribe configSubscribe, JSONObject searchData) {
        return false;
    }

}
