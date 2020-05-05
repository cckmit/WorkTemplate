package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGameSetMapper;
import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.second.mapper.ConfigProgramMapper;
import com.fish.dao.second.model.ConfigProgram;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
    ConfigProgramMapper configProgramMapper;
    @Autowired
    ArcadeGameSetMapper arcadeGameSetMapper;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    WxConfigService wxConfigService;

    /**
     * 查询
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ConfigProgram> selectAll(GetParameter parameter) {
        List<ConfigProgram> configPrograms = configProgramMapper.selectAll();
        ConcurrentHashMap<String, WxConfig> wxConfigMap = wxConfigService.getAll(WxConfig.class);
        for (ConfigProgram configProgram : configPrograms) {
            String ddAppId = configProgram.getDdAppId();
            WxConfig wxConfig = wxConfigMap.get(ddAppId);
            if (wxConfig != null) {
                configProgram.setProductName(wxConfig.getProductName());
                configProgram.setProgramType(wxConfig.getProgramType());
            }
            ArcadeGameSet arcadeGameSet = arcadeGameSetMapper.selectByPrimaryKey(configProgram.getDdCode());
            if (arcadeGameSet != null) {
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
        if (null == record.getDdCode()) {
            result.setSuccessed(false);
            result.setMsg("请填写正式合集后提交");
            return result;
        }
        ConfigProgram configProgram = configProgramMapper.selectByPrimaryKey(record.getDdAppId(), record.getDdMinVer());
        if (configProgram != null) {
            result.setSuccessed(false);
            result.setMsg("包含重复产品和最低版本号组合");
            return result;
        }
        int insert = 0;
        try {
            insert = configProgramMapper.insert(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.error("合集及版本号配置新增异常" + ", 详细信息:{}", e.getMessage());
        }
        if (insert == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("config_program", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，联系管理员");
        }
        return result;
    }

    /**
     * 更新
     *
     * @param record
     * @return
     */
    public PostResult updateByPrimaryKeySelective(ConfigProgram record) {
        PostResult result = new PostResult();
        record.setTimes(new Timestamp(System.currentTimeMillis()));
        if (null == record.getDdCode()) {
            result.setSuccessed(false);
            result.setMsg("请填写正式合集后提交");
            return result;
        }
        int insert = 0;
        try {
            insert = configProgramMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            LOGGER.error("合集及版本号配置修改异常" + ", 详细信息:{}", e.getMessage());
        }
        if (insert == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("config_program", baseConfig.getFlushCache());
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
