package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.BuyPay;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BuyPayMapper {
    int deleteByPrimaryKey(@Param("buyDate") String date, @Param("buyAppId") String appId);

    int insert(BuyPay record);

    int insertSelective(BuyPay record);

    BuyPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BuyPay record);

    int updateByPrimaryKey(BuyPay record);

    BuyPay selectByAppIdAndDate(@Param("buyDate") String date, @Param("buyAppId") String appId);

    /**
     * 通过时间段查买量数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<BuyPay> selectBuyPayByDate(@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    BigDecimal selectCountBuyCost(String date);

    List<BuyPay> selectAll();

    int insertBatch(List<BuyPay> lists);

    List<BuyPay> selectSearch(String SQL);
}