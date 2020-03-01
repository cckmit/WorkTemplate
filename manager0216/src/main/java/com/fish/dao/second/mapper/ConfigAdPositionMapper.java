package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdPosition;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 17:31
 */
public interface ConfigAdPositionMapper {

    /**
     * 查询指定广告内容
     *
     * @param id
     * @return
     */
    ConfigAdPosition select(int id);

    /**
     * 查询全部广告
     *
     * @return
     */
    List<ConfigAdPosition> selectAll();

    /**
     * 新增广告内容
     *
     * @param adContent
     * @return
     */
    int insert(ConfigAdPosition adContent);

    /**
     * 修改广告内容
     *
     * @param adContent
     * @return
     */
    int update(ConfigAdPosition adContent);

    /**
     * 根据ID删除广告内容
     *
     * @param id
     * @return
     */
    int delete(int id);

}
