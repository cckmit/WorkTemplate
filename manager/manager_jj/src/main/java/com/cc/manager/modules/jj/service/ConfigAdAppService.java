package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdApp;
import com.cc.manager.modules.jj.entity.ConfigAdCombination;
import com.cc.manager.modules.jj.entity.ConfigAdStrategy;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.ConfigAdAppMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:36
 */
@Service
@DS("jj")
public class ConfigAdAppService extends BaseCrudService<ConfigAdApp, ConfigAdAppMapper> {

    private WxConfigService wxConfigService;
    private ConfigAdCombinationService configAdCombinationService;
    private ConfigAdStrategyService configAdStrategyService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdApp> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("appId");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
            String minVersion = queryObject.getString("minVersion");
            queryWrapper.like(StringUtils.isNotBlank(minVersion), "ddMinVersion", minVersion);
            String combinationId = queryObject.getString("combinationId");
            queryWrapper.eq(StringUtils.isNotBlank(combinationId), "ddCombinationId", combinationId);
        }
    }

    @Override
    protected void updateInsertEntity(String requestParam, ConfigAdApp entity) {
        this.autoSetAdUnit(entity);
        super.updateInsertEntity(requestParam, entity);
    }

    @Override
    protected boolean update(String requestParam, ConfigAdApp entity, UpdateWrapper<ConfigAdApp> updateWrapper) {
        this.autoSetAdUnit(entity);
        updateWrapper.eq("ddAppId", entity.getAppId()).eq("ddMinVersion", entity.getMinVersion());
        return this.update(entity, updateWrapper);
    }

    /**
     * 从app配置模块自动更新广告内容
     *
     * @param entity ConfigAdApp
     */
    private void autoSetAdUnit(ConfigAdApp entity) {
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, entity.getAppId());
        if (StringUtils.isBlank(entity.getWxBannerUnit())) {
            String banner = wxConfig.getBanner();
            if (StringUtils.isNotBlank(banner) && StringUtils.contains(banner, "-")) {
                banner = StringUtils.split(banner, "-")[1];
            }
            entity.setWxBannerUnit(banner);
        }
        if (StringUtils.isBlank(entity.getWxIntUint())) {
            String intUnit = wxConfig.getScreen();
            if (StringUtils.isNotBlank(intUnit) && StringUtils.contains(intUnit, "-")) {
                intUnit = StringUtils.split(intUnit, "-")[1];
            }
            entity.setWxIntUint(intUnit);
        }
        if (StringUtils.isBlank(entity.getWxReVideoUnit())) {
            String reVideo = wxConfig.getVideo();
            if (StringUtils.isNotBlank(reVideo) && StringUtils.contains(reVideo, "-")) {
                reVideo = StringUtils.split(reVideo, "-")[1];
            }
            entity.setWxReVideoUnit(reVideo);
        }
    }

    @Override
    protected void rebuildSelectedEntity(ConfigAdApp entity) {
        entity.setAppName(this.wxConfigService.getCacheValue(WxConfig.class, entity.getAppId()));
        entity.setCombinationName(this.configAdCombinationService.getCacheValue(ConfigAdCombination.class, String.valueOf(entity.getCombinationId())));
        entity.setWxBannerStrategyName(this.configAdStrategyService.getCacheValue(ConfigAdStrategy.class, String.valueOf(entity.getWxBannerStrategyId())));
        entity.setWxIntStrategyName(this.configAdStrategyService.getCacheValue(ConfigAdStrategy.class, String.valueOf(entity.getWxIntStrategyId())));
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdApp> deleteWrapper) {
        int count = 0;
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> idList = JSONObject.parseArray(requestParam, String.class);
            for (String appIdAndMinVersion : idList) {
                String[] split = appIdAndMinVersion.split("-");
                UpdateWrapper<ConfigAdApp> removeWrapper = new UpdateWrapper<>();
                removeWrapper.eq("ddAppId", split[0]).eq("ddMinVersion", split[1]);
                boolean remove = this.remove(removeWrapper);
                if (remove) {
                    count++;
                }
            }
            return count == idList.size();
        }
        return false;
    }

    /**
     * 更新状态
     *
     * @param requestParam 请求参数
     * @return 执行结果
     */
    public PostResult switchStatus(String requestParam) {
        PostResult postResult = new PostResult();
        try {
            JSONObject requestObject = JSONObject.parseObject(requestParam);
            String switchColumn = requestObject.getString("switchColumn");
            boolean status = requestObject.getBoolean("status");
            UpdateWrapper<ConfigAdApp> updateWrapper = new UpdateWrapper<>();
            if (StringUtils.equals("allowedShow", switchColumn)) {
                updateWrapper.set("ddAllowedShow", status);
            } else if (StringUtils.equals("wxBannerAllowedShow", switchColumn)) {
                updateWrapper.set("ddWxBannerAllowedShow", status);
            } else if (StringUtils.equals("wxIntAllowedShow", switchColumn)) {
                updateWrapper.set("ddWxIntAllowedShow", status);
            } else if (StringUtils.equals("wxReVideoAllowedShow", switchColumn)) {
                updateWrapper.set("ddWxReVideoAllowedShow", status);
            } else {
                updateWrapper = null;
            }
            if (updateWrapper != null) {
                updateWrapper.eq("ddAppId", requestObject.getString("appId")).eq("ddMinVersion", requestObject.getString("minVersion"));
                if (!this.update(updateWrapper)) {
                    postResult.setCode(2);
                    postResult.setMsg("操作失败！");
                }
            }
        } catch (RuntimeException e) {
            postResult.setCode(2);
            postResult.setMsg("操作失败！");
        }
        return postResult;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setConfigAdCombinationService(ConfigAdCombinationService configAdCombinationService) {
        this.configAdCombinationService = configAdCombinationService;
    }

    @Autowired
    public void setConfigAdStrategyService(ConfigAdStrategyService configAdStrategyService) {
        this.configAdStrategyService = configAdStrategyService;
    }

}
