package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxDailySummaryMapper;
import com.blazefire.dao.second.model.WxDailySummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxDailySummary service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:29
 */
@Service
public class WxDailySummaryService {

    private WxDailySummaryMapper wxDailySummaryMapper;

    /**
     * 存储小程序数据概况
     *
     * @param wxDailySummary 小程序数据概况
     */
    public void insert(WxDailySummary wxDailySummary) {
        if (wxDailySummary != null) {
            this.wxDailySummaryMapper.delete(wxDailySummary);
            this.wxDailySummaryMapper.insert(wxDailySummary);
        }
    }

    @Autowired
    public void setWxDailySummaryMapper(WxDailySummaryMapper wxDailySummaryMapper) {
        this.wxDailySummaryMapper = wxDailySummaryMapper;
    }
}
