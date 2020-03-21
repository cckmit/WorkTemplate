package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.ConfigProgramMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.ConfigProgram;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 合集及版本号配置
 * ConfigProgramService
 *
 * @author
 * @date
 */
@Service
public class ConfigProgramService implements BaseService<ConfigProgram> {


    @Autowired
    CacheService cacheService;
    @Autowired
    ConfigProgramMapper configProgramMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ConfigProgram> selectAll(GetParameter parameter) {
        List<ConfigProgram> configPrograms = configProgramMapper.selectAll();
        for (ConfigProgram configProgram : configPrograms) {
            String ddAppId = configProgram.getDdAppId();
            WxConfig wxConfig = cacheService.getWxConfig(ddAppId);
            if(wxConfig !=null){
                configProgram.setProductName(wxConfig.getProductName());
                configProgram.setProgramType(wxConfig.getProgramType());
            }
            ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(configProgram.getDdCode());
            if(arcadeGameSet !=null){
                configProgram.setCodename(arcadeGameSet.getDdname());
            }
        }
        return configPrograms;
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    public PostResult insert(ConfigProgram record) {
        PostResult result = new PostResult();
        record.setTimes(new Timestamp(System.currentTimeMillis()));
        int insert = 0;
        try {
            insert = configProgramMapper.insert(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (insert == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("config_program", baseConfig.getFlushCache());
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("包含重复数据，请检查后提交");
        }
        return result;
    }

    /**
     * 更新appConfig信息
     *
     * @param record
     * @return
     */
    public PostResult updateByPrimaryKeySelective(ConfigProgram record) {

        PostResult result = new PostResult();
        record.setTimes(new Timestamp(System.currentTimeMillis()));
        int insert = 0;
        try {
            insert = configProgramMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (insert == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("config_program", baseConfig.getFlushCache());
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("修改失败，请联系管理员！");
        }
        return result;
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
    public Class<ConfigProgram> getClassInfo() {
        return ConfigProgram.class;
    }

    /**
     * 筛选
     *
     * @param searchData
     * @return
     */
    @Override
    public boolean removeIf(ConfigProgram configProgram, JSONObject searchData) {
        if ("".equals(searchData.getString("appId"))) {
            return false;
        }
        if (existValueFalse(configProgram.getDdAppId(), searchData.getString("appId"))) {
            return true;
        }
        return false;
    }

    /**
     * 审核合集下拉框
     *
     * @param ddAppId, ddMinVer
     * @return
     */
    public ConfigProgram select(String ddAppId, String ddMinVer) {
        return configProgramMapper.selectByPrimaryKey(ddAppId, ddMinVer);
    }

    /**
     * 删除
     *
     * @param ddAppId, ddMinVer
     * @return
     */
    public PostResult delete(String ddAppId, String ddMinVer) {
        PostResult result = new PostResult();
        int delete = this.configProgramMapper.deleteByPrimaryKey(ddAppId, ddMinVer);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("删除失败，请联系管理员！");
        } else {
            ReadJsonUtil.flushTable("config_program", this.baseConfig.getFlushCache());
        }
        return result;
    }
}
