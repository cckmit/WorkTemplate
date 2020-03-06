package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdWx;

import java.util.List;

/**
 * 广告类型Mybatis-Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:52
 */
public interface ConfigAdWxMapper {

    /**
     * 通过ID查询广告类型
     *
     * @param id
     * @return
     */
    List<ConfigAdWx> select(int id);

    /**
     * 查询全部广告类型
     *
     * @return
     */
    List<ConfigAdWx> selectAll();

    /**
     * 新增广告类型
     *
     * @param configAdType
     * @return
     */
    int insert(ConfigAdWx configAdType);

    /**
     * 修改广告类型
     *
     * @param configAdType
     * @return
     */
    int update(ConfigAdWx configAdType);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(int id);

}
