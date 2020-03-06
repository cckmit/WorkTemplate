package com.fish.dao.second.mapper;

import com.fish.dao.second.model.WxGroupManager;

import java.util.List;

public interface WxGroupManagerMapper {

    List<WxGroupManager> selectAll();

    /**
     *  关联查询查看所有信息
     * @param sql
     * @return
     */
    List<WxGroupManager> selectAllConfigSQL(String sql);

    /**
     * 更新wx_group_manager表
     * @param wxGroupManager
     * @return
     */
    int updateWxGroupManager(WxGroupManager wxGroupManager);

    /**
     *  更新config_confirm表
     * @param wxGroupManager
     * @return
     */
    int updateConfigConfirm(WxGroupManager wxGroupManager);

    /**
     * 查询群二维码和客服二维码
     * @param wxGroupManager
     * @return
     */
    WxGroupManager selectQrCodeByPrimaryKey(WxGroupManager wxGroupManager);

    /**
     * 插入历史数据
     * @param wxGroupManager
     * @return
     */
    int insertHistoryDate(WxGroupManager wxGroupManager);

    int changeStatus(Integer ddStatus);
}
