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
     * @param id
     * @return
     */
    ConfigAdApp select(int id);

    /**
     * 查询全部广告类型
     *
     * @return
     */
    List<ConfigAdApp> selectAll();

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
     * @param deleteIds
     * @return
     */
    int delete(String deleteIds);

    /**
     * 通过页面开关改变运营状态
     * @param id
     * @param ddAllowedShow
     * @return
     */
    int changeAllowedShowStatus(Integer id, Boolean ddAllowedShow);
}
