package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdSpace;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 17:31
 */
public interface ConfigAdSpaceMapper {

    /**
     * 查询指定广告内容
     *
     * @param id
     * @return
     */
    ConfigAdSpace select(int id);

    /**
     * 查询全部广告
     *
     * @param adSpace
     * @return
     */
    List<ConfigAdSpace> selectAll(ConfigAdSpace adSpace);

    /**
     * 查询全部广告
     *
     * @param spaceIds
     * @return
     */
    List<ConfigAdSpace> selectAllByIds(String spaceIds);

    /**
     * 新增广告内容
     *
     * @param adSpace
     * @return
     */
    int insert(ConfigAdSpace adSpace);

    /**
     * 修改广告内容
     *
     * @param adSpace
     * @return
     */
    int update(ConfigAdSpace adSpace);

    /**
     * 根据ID删除广告位
     *
     * @param deleteIds
     * @return
     */
    int delete(String deleteIds);

}
