package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.AdValue;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 记录广告数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-09 22:57
 */
@Repository
public interface AdValueMapper {

    /**
     * 查询已完成数据分析的最大时间
     *
     * @return 已完成数据分析的最大时间
     */
    String queryMaxAnalysisHour();

    /**
     * 新增和更新广告数据
     *
     * @param adValueList 广告数据集合
     */
    void insert(@Param("adValueList") List<AdValue> adValueList);

    /**
     * 删除一小时的数据
     *
     * @param hourNum 小时段
     */
    void deleteOneHourAdValue(@Param("hourNum") int hourNum);

}
