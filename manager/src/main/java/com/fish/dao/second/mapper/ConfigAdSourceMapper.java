package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdSource;

import java.util.List;

/**
 * 广告来源Mybatis-Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:51
 */
public interface ConfigAdSourceMapper {

    /**
     * 通过ID查询广告来源
     *
     * @param id
     * @return
     */
    ConfigAdSource select(int id);

    /**
     * 查询全部广告源
     *
     * @return
     */
    List<ConfigAdSource> selectAll();

    /**
     * 新增广告来源
     *
     * @param configAdSource
     * @return
     */
    int insert(ConfigAdSource configAdSource);

    /**
     * 修改广告来源
     *
     * @param configAdSource
     * @return
     */
    int update(ConfigAdSource configAdSource);

}
