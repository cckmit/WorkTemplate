package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxVisitTrendMapper;
import com.blazefire.dao.second.model.WxVisitTrend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxVisitTrend Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:48
 */
@Service
public class WxVisitTrendService {

    private WxVisitTrendMapper wxVisitTrendMapper;

    /**
     * 插入插入小程序数据日/月趋势
     *
     * @param wxVisitTrend 插入小程序数据日/月趋势
     */
    public void insertDaily(WxVisitTrend wxVisitTrend) {
        if (wxVisitTrend != null) {
            this.wxVisitTrendMapper.deleteDaily(wxVisitTrend);
            this.wxVisitTrendMapper.insertDaily(wxVisitTrend);
        }
    }

    /**
     * 插入插入小程序数据日/月趋势
     *
     * @param wxVisitTrend 插入小程序数据日/月趋势
     */
    public void insertMonthly(WxVisitTrend wxVisitTrend) {
        if (wxVisitTrend != null) {
            this.wxVisitTrendMapper.deleteMonthly(wxVisitTrend);
            this.wxVisitTrendMapper.insertMonthly(wxVisitTrend);
        }
    }

    /**
     * 插入插入小程序数据日/月趋势
     *
     * @param wxVisitTrend 插入小程序数据日/月趋势
     */
    public void insertWeekly(WxVisitTrend wxVisitTrend) {
        if (wxVisitTrend != null) {
            this.wxVisitTrendMapper.deleteWeekly(wxVisitTrend);
            this.wxVisitTrendMapper.insertWeekly(wxVisitTrend);
        }
    }

    @Autowired
    public void setWxVisitTrendMapper(WxVisitTrendMapper wxVisitTrendMapper) {
        this.wxVisitTrendMapper = wxVisitTrendMapper;
    }

}
