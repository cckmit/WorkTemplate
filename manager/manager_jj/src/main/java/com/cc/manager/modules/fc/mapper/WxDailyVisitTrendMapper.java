package com.cc.manager.modules.fc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.entity.WxDailyVisitTrend;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-14
 */
@Repository
public interface WxDailyVisitTrendMapper extends BaseMapper<WxDailyVisitTrend> {


    /**
     * 查询小程序产品数据
     * @param start start
     * @param end end
     * @param ddAppId ddAppId
     * @return List
     */
    List<MinitjWx> selectVisitTrend(String start, String end, String ddAppId);

    /**
     *  小程序汇总数据
     * @param start start
     * @param end end
     * @return List
     */
    List<MinitjWx> selectVisitTrendSummary(String start, String end);

}
