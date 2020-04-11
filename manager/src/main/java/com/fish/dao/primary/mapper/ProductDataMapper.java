package com.fish.dao.primary.mapper;

import com.fish.dao.third.model.ProductData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface ProductDataMapper {
    int deleteByPrimaryKey(@Param("wxAppid") String wxAppId, @Param("wxDate") Date wxDate);

    int insert(ProductData record);

    int insertSelective(ProductData record);

    ProductData selectByPrimaryKey(@Param("wxAppid") String wxAppid, @Param("wxDate") Date wxDate);

    ProductData selectByAppid(@Param("wxAppid") String wxAppId, @Param("wxDate") String wxDate);

    List<ProductData> searchProgramData(String wxAppid, String start,String end);

    /**
     * 根据日期查询小程序数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductData> queryProgramByDate(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    int updateByPrimaryKeySelective(ProductData record);

    int updateByPrimaryKey(ProductData record);

    void insertBatch(List<ProductData> lists);

    List<String> allDate();

}