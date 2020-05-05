package com.fish.dao.third.mapper;

import com.fish.dao.third.model.MinitjWx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MinitjWxMapper {

    int insert(MinitjWx record);

    MinitjWx selectByPrimaryKey(@Param("wxAppid") String wxAppid, @Param("wxDate") String wxDate);

    List<MinitjWx> searchData(String sql);

    List<MinitjWx> searchDatas(@Param("wxAppid") String wxAppid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 根据日期查询小游戏数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<MinitjWx> queryMinitjWxByDate(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<String> dateCash();
}