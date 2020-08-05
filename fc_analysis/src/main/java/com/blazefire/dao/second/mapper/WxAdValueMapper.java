package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.AdValueWx;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Wx广告数据 Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-29 22:21
 */
@Repository
public interface WxAdValueMapper {

    /**
     * 保存微信广告汇总数据
     *
     * @param adValueWxList 微信广告数据列表
     */
    void insertAdposGeneral(@Param("adValueWxList") List<AdValueWx> adValueWxList);

    /**
     * 保存微信广告细分数据
     *
     * @param adValueWxList 微信广告数据列表
     */
    void insertAdunitGeneral(@Param("adValueWxList") List<AdValueWx> adValueWxList);

    /**
     * 删除微信广告汇总数据
     *
     * @param adValueWx 微信广告数据
     * @return 删除结果
     */
    int deleteAdposGeneral(AdValueWx adValueWx);

    /**
     * 删除微信广告细分数据
     *
     * @param adValueWx 微信广告数据
     * @return
     */
    int deleteAdunitGeneral(AdValueWx adValueWx);

    /**
     * 查询广告数据
     *
     * @param appId     appId
     * @param beginDate 查询开始时间，要求yyyy-MM-dd
     * @param endDate   查询结束时间，要求yyyy-MM-dd
     * @return 微信广告数据结果
     */
    List<AdValueWx> queryAdValueWx(@Param("appId") String appId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

}
