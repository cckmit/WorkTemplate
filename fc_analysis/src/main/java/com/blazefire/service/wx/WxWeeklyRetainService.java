package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxWeeklyRetainMapper;
import com.blazefire.dao.second.model.WxWeeklyRetain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxWeeklyRetain Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 23:25
 */
@Service
public class WxWeeklyRetainService {

    private WxWeeklyRetainMapper wxWeeklyRetainMapper;

    public void insert(WxWeeklyRetain wxWeeklyRetain) {
        if (wxWeeklyRetain != null) {
            this.wxWeeklyRetainMapper.insert(wxWeeklyRetain);
        }
    }

    @Autowired
    public void setWxWeeklyRetainMapper(WxWeeklyRetainMapper wxWeeklyRetainMapper) {
        this.wxWeeklyRetainMapper = wxWeeklyRetainMapper;
    }

}
