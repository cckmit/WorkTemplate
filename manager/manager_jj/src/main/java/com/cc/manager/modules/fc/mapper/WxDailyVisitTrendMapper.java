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

    @Select("  SELECT t.appId AS wxAppid,  t.visitPv AS wxVisit,  t.visitUv AS wxActive,  t.visitUvNew AS wxNew,\n" +
            "        ROUND(t.stayTimeUv,0) AS wxAvgOnline, DATE(t.refDate) AS wxDate, s.shareUv AS wxShareUser, s.sharePv AS wxShareCount, (r.day1/r.day0) AS wxRemain2\n" +
            "        FROM wx_daily_visit_trend  AS t LEFT JOIN wx_daily_summary AS s ON t.appId = s.appId AND t.refDate =s.refDate LEFT JOIN\n" +
            "         wx_daily_retain AS r ON t.appId = r.appId AND t.refDate =r.refDate\n" +
            "        WHERE DATE(t.refDate) BETWEEN   #{start} AND #{end} AND r.dataType ='visit_uv_new'\n" +
            "        GROUP BY t.refDate,t.appId,  t.visitPv,  t.visitUv,  t.visitUvNew,\n" +
            "        t.stayTimeUv,s.shareUv, s.sharePv,(r.day1/r.day0)")
    List<MinitjWx> selectVisitTrend(String start, String end);


    @Select("  SELECT t.appId AS wxAppid,  t.visitPv AS wxVisit,  t.visitUv AS wxActive,  t.visitUvNew AS wxNew,\n" +
            "        ROUND(t.stayTimeUv,0) AS wxAvgOnline, DATE(t.refDate) AS wxDate, s.shareUv AS wxShareUser, s.sharePv AS wxShareCount, (r.day1/r.day0) AS wxRemain2\n" +
            "        FROM wx_daily_visit_trend  AS t LEFT JOIN wx_daily_summary AS s ON t.appId = s.appId AND t.refDate =s.refDate LEFT JOIN\n" +
            "         wx_daily_retain AS r ON t.appId = r.appId AND t.refDate =r.refDate\n" +
            "        WHERE DATE(t.refDate) BETWEEN   #{start} AND #{end} AND r.dataType ='visit_uv_new'\n" +
            "        AND t.appId = #{appId, jdbcType=VARCHAR}\n" +
            "        GROUP BY t.refDate,t.appId,  t.visitPv,  t.visitUv,  t.visitUvNew,\n" +
            "        t.stayTimeUv,s.shareUv, s.sharePv,(r.day1/r.day0)")
    List<MinitjWx> selectVisitTrend(String start, String end, String appId);
}
