package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdCombination;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-13 17:03
 */
public interface ConfigAdCombinationMapper {
    /**
     * 查询全部组合
     *
     * @return
     */
    List<ConfigAdCombination> selectAll();

    /**
     * 查询指定组合
     *
     * @param ddId
     * @return
     */
    ConfigAdCombination select(int ddId);

    /**
     * 新增数据
     *
     * @param configAdCombination
     * @return
     */
    int insert(ConfigAdCombination configAdCombination);

    /**
     * 更新数据
     *
     * @param configAdCombination
     * @return
     */
    int update(ConfigAdCombination configAdCombination);

    /**
     * 更新广告组合Json配置
     *
     * @param configAdCombination
     * @return
     */
    int saveCombinationJson(ConfigAdCombination configAdCombination);

    /**
     * 删除组合数据
     *
     * @param deleteIds
     * @return
     */
    int delete(String deleteIds);

}
