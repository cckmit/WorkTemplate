package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxDailyRetainMapper;
import com.blazefire.dao.second.model.WxDailyRetain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxDailyRetain service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:05
 */
@Service
public class WxDailyRetainService {

    private WxDailyRetainMapper wxDailyRetainMapper;

    public void insert(WxDailyRetain wxDailyRetain) {
        if (wxDailyRetain != null) {
            this.wxDailyRetainMapper.delete(wxDailyRetain);
            this.wxDailyRetainMapper.insert(wxDailyRetain);
        }
    }

    @Autowired
    public void setWxDailyRetainMapper(WxDailyRetainMapper wxDailyRetainMapper) {
        this.wxDailyRetainMapper = wxDailyRetainMapper;
    }

}
