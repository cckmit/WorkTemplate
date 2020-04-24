package com.fish.dao.second.mapper;

import com.fish.dao.second.model.ConfigAdContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 17:31
 */
public interface ConfigAdContentMapper {

    /**
     * 查询指定广告内容
     *
     * @param id
     * @return
     */
    ConfigAdContent select(int id);

    /**
     * 查询全部广告
     *
     * @param configAdContent
     * @return
     */
    List<ConfigAdContent> selectAll(ConfigAdContent configAdContent);

    /**
     * 根据广告类型查询广告内容
     *
     * @param adType
     * @return
     */
    List<ConfigAdContent> selectAllByType(int adType);

    /**
     * 通过广告内容ID查询广告内容详情
     *
     * @param contentIds
     * @return
     */
    List<ConfigAdContent> selectContentBySpaceId(String contentIds);

    /**
     * 新增广告内容
     *
     * @param adContent
     * @return
     */
    int insert(ConfigAdContent adContent);

    /**
     * 修改广告内容
     *
     * @param adContent
     * @return
     */
    int update(ConfigAdContent adContent);

    /**
     * 根据ID删除广告内容
     *
     * @param deleteIds
     * @return
     */
    int delete(String deleteIds);

    /**
     * 更新广告图片地址
     *
     * @param imageUrl
     * @param id
     */
    void updateImageUrl(@Param("imageUrl") String imageUrl, @Param("id") int id);

    /**
     * 推广App下拉框选项
     * @return App合集
     */
    List<ConfigAdContent> getTargetAndPromoteAppInfo();
}
