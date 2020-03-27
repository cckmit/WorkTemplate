package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.WxGroupManagerMapper;
import com.fish.dao.second.model.WxGroupManager;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信群管理 Service
 * WxGroupManagerService
 *
 * @author
 * @date
 */
@Service
public class WxGroupManagerService implements BaseService<WxGroupManager> {

    @Autowired
    WxGroupManagerMapper wxGroupManagerMapper;

    /**
     * 查询
     *
     * @param parameter
     * @return
     */
    @Override
    public List<WxGroupManager> selectAll(GetParameter parameter) {
        return wxGroupManagerMapper.selectAll();
    }

    /**
     * 更新
     *
     * @param wxGroupManager
     * @return
     */
    public int updateWxGroupManager(WxGroupManager wxGroupManager) {
        return wxGroupManagerMapper.updateWxGroupManager(wxGroupManager);
    }

    /**
     * 更新 ConfigConfirm
     *
     * @param wxGroupManager
     * @return
     */
    public int updateConfigConfirm(WxGroupManager wxGroupManager) {
        return wxGroupManagerMapper.updateConfigConfirm(wxGroupManager);
    }

    /**
     * 判断是否更新上传时间
     *
     * @param wxGroupManager
     * @return
     */
    public boolean isUpdateOperateTime(WxGroupManager wxGroupManager) {
        boolean isUpdate = false;
        WxGroupManager wxGroupManager1 = wxGroupManagerMapper.selectQrCodeByPrimaryKey(wxGroupManager);
        if (!wxGroupManager.getDdYes().equals(wxGroupManager1.getDdYes())
                || !wxGroupManager.getDdNo().equals(wxGroupManager1.getDdNo())) {
            isUpdate = true;
        }
        return isUpdate;
    }

    /**
     * 历史记录插入历史表
     *
     * @param wxGroupManager
     * @return
     */
    public int insertHistoryDate(WxGroupManager wxGroupManager) {
        return wxGroupManagerMapper.insertHistoryDate(wxGroupManager);
    }

    public int changeStatus(Integer ddStatus) {
        return wxGroupManagerMapper.changeStatus(ddStatus);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setOrder("desc");
        parameter.setSort("id");
    }

    @Override
    public Class<WxGroupManager> getClassInfo() {
        return WxGroupManager.class;
    }

    @Override
    public boolean removeIf(WxGroupManager wxGroupManager, JSONObject searchData) {
        return false;
    }
}
