package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigConfirm;
import com.cc.manager.modules.jj.entity.WxGroupHistory;
import com.cc.manager.modules.jj.entity.WxGroupManager;
import com.cc.manager.modules.jj.mapper.WxGroupManagerMapper;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class WxGroupManagerService extends BaseCrudService<WxGroupManager, WxGroupManagerMapper> {

    private ConfigConfirmService configConfirmService;
    private WxGroupHistoryService wxGroupHistoryService;
    private PersieServerUtils persieServerUtils;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxGroupManager> queryWrapper) {

    }

    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<WxGroupManager> entityList) {
        for (WxGroupManager wxGroupManager : entityList) {
            ConfigConfirm configConfirm = this.configConfirmService.getById(wxGroupManager.getCdId());
            if (configConfirm != null) {
                wxGroupManager.setUpdateTime(configConfirm.getUpdateTime());
                wxGroupManager.setDdDescribe(configConfirm.getDdDescribe());
                wxGroupManager.setDdYes(configConfirm.getDdYes());
                wxGroupManager.setDdNo(configConfirm.getDdNo());
                wxGroupManager.setDdStatus(configConfirm.getDdStatus());
            }
        }
    }

    @Override
    protected boolean update(String requestParam, WxGroupManager entity, UpdateWrapper<WxGroupManager> updateWrapper) {
        ConfigConfirm configConfirmById = this.configConfirmService.getById(entity.getCdId());
        entity.setDdStatus(configConfirmById.getDdStatus());
        //把操作记录保存到历史记录表
        WxGroupHistory wxGroupHistory = new WxGroupHistory();
        BeanUtils.copyProperties(entity, wxGroupHistory);
        wxGroupHistory.setUpdateTime(LocalDateTime.now());
        if (entity.getDdStatus()) {
            wxGroupHistory.setDdStatus(1);
        } else {
            wxGroupHistory.setDdStatus(0);
        }
        wxGroupHistoryService.save(wxGroupHistory);
        // 判断是否需要更新上传二维码时间
        if (isUpdateOperateTime(entity)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String updateRqCodeTime = format.format(new Date());
            entity.setUpdateQrCodeTime(updateRqCodeTime);
        }
        updateWrapper.eq("id", entity.getId());
        boolean update = this.update(entity, updateWrapper);
        if (update) {
            ConfigConfirm configConfirm = new ConfigConfirm();
            BeanUtils.copyProperties(entity, configConfirm);
            configConfirm.setUpdateTime(LocalDateTime.now());
            UpdateWrapper<ConfigConfirm> configConfirmUpdateWrapper = new UpdateWrapper<>();
            configConfirmUpdateWrapper.eq("ddId", entity.getCdId());
            boolean configConfirmUpdate = configConfirmService.update(configConfirm, configConfirmUpdateWrapper);
            if (configConfirmUpdate) {
                this.persieServerUtils.refreshTable("config_confirm");
            }
            return configConfirmService.update(configConfirm, configConfirmUpdateWrapper);
        }
        return false;
    }

    /**
     * 判断是否更新上传时间
     *
     * @param wxGroupManager wxGroupManager
     * @return boolean
     */
    private boolean isUpdateOperateTime(WxGroupManager wxGroupManager) {
        boolean isUpdate = false;
        ConfigConfirm configConfirm = this.configConfirmService.getCacheEntity(ConfigConfirm.class, wxGroupManager.getCdId());
        if (!wxGroupManager.getDdYes().equals(configConfirm.getDdYes())
                || !wxGroupManager.getDdNo().equals(configConfirm.getDdNo())) {
            isUpdate = true;
        }
        return isUpdate;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxGroupManager> deleteWrapper) {
        return false;
    }

    /**
     * 通过页面开关改变状态
     *
     * @param jsonObject jsonObject
     * @return PostResult
     */
    public PostResult switchStatus(JSONObject jsonObject) {
        PostResult result = new PostResult();
        ConfigConfirm configConfirm = new ConfigConfirm();
        UpdateWrapper<ConfigConfirm> configConfirmUpdateWrapper = new UpdateWrapper<>();
        String id = jsonObject.getString("id");
        Boolean status = jsonObject.getBoolean("status");
        configConfirm.setDdStatus(status);
        configConfirmUpdateWrapper.eq("ddId", id);
        boolean update = configConfirmService.update(configConfirm, configConfirmUpdateWrapper);
        if (!update) {
            result.setCode(2);
            result.setMsg("操作失败，变更开关状态失败！");
        }
        return result;
    }

    @Autowired
    public void setConfigConfirmService(ConfigConfirmService configConfirmService) {
        this.configConfirmService = configConfirmService;
    }

    @Autowired
    public void setWxGroupHistoryService(WxGroupHistoryService wxGroupHistoryService) {
        this.wxGroupHistoryService = wxGroupHistoryService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}
