package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.AdValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 广告数据查询
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-10 13:48
 */
public interface AdValueMapper {

    /**
     * 查询全部广告数据
     *
     * @param beginDate
     * @param endDate
     * @param adValue
     * @return
     */
    List<AdValue> selectAll(@Param("beginDate") int beginDate, @Param("endDate") int endDate,
            @Param("adValue") AdValue adValue);

}
