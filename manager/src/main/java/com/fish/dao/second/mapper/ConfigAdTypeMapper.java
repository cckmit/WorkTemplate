package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdType;

import java.util.List;

/**
 * 广告类型Mybatis-Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:52
 */
public interface ConfigAdTypeMapper {

    /**
     * 通过ID查询广告类型
     *
     * @param id
     * @return
     */
    ConfigAdType select(int id);

    /**
     * 查询全部广告类型
     *
     * @return
     */
    List<ConfigAdType> selectAll();

    /**
     * 新增广告类型
     *
     * @param configAdType
     * @return
     */
    int insert(ConfigAdType configAdType);

    /**
     * 修改广告类型
     *
     * @param configAdType
     * @return
     */
    int update(ConfigAdType configAdType);

}
