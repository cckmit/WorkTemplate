package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdApp;

import java.util.List;

/**
 * 广告类型Mybatis-Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:52
 */
public interface ConfigAdAppMapper {

    /**
     * 通过ID查询广告类型
     *
     * @param ddAppId appId
     * @return
     */
    ConfigAdApp select(String ddAppId, String ddMinVersion);

    /**
     * 查询全部广告类型
     *
     * @return
     */
    List<ConfigAdApp> selectAll();

    /**
     * 新增更新广告类型
     *
     * @param configAdType configAdType
     * @return 处理结果
     */
    int save(ConfigAdApp configAdType);

    /**
     * 新增广告类型
     *
     * @param configAdType
     * @return
     */
    int insert(ConfigAdApp configAdType);

    /**
     * 修改广告类型
     *
     * @param configAdType
     * @return
     */
    int update(ConfigAdApp configAdType);

    /**
     * 根据ID删除微信广告配置
     *
     * @param ddAppId ddAppId
     * @return
     */
    int delete(String ddAppId, String ddMinVersion);

    /**
     * 通过页面开关改变运营状态
     *
     * @param ddAppId
     * @param ddAllowedShow
     * @return
     */
    int changeAllowedShowStatus(String ddAppId, String ddMinVersion, Boolean ddAllowedShow);

    /**
     * 通过页面开关改变Banner状态
     *
     * @param ddAppId
     * @param ddWxBannerAllowedShow
     * @return
     */
    int changeBannerStatus(String ddAppId, String ddMinVersion, Boolean ddWxBannerAllowedShow);

    /**
     * 通过页面开关改变插屏状态
     *
     * @param ddAppId
     * @param ddWxIntAllowedShow
     * @return
     */
    int changeIconStatus(String ddAppId, String ddMinVersion, Boolean ddWxIntAllowedShow);

    /**
     * 通过页面开关改变激励视频状态
     *
     * @param ddAppId
     * @param ddWxReVideoAllowedShow
     * @return
     */
    int changeVideoStatus(String ddAppId, String ddMinVersion, Boolean ddWxReVideoAllowedShow);

}
