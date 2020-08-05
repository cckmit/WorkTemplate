package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxMonthlyRetainMapper;
import com.blazefire.dao.second.model.WxMonthlyRetain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxMonthlyRetain service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:05
 */
@Service
public class WxMonthlyRetainService {

    private WxMonthlyRetainMapper wxMonthlyRetainMapper;

    public void insert(WxMonthlyRetain wxMonthlyRetain) {
        if (wxMonthlyRetain != null) {
            this.wxMonthlyRetainMapper.delete(wxMonthlyRetain);
            this.wxMonthlyRetainMapper.insert(wxMonthlyRetain);
        }
    }

    @Autowired
    public void setWxMonthlyRetainMapper(WxMonthlyRetainMapper wxMonthlyRetainMapper) {
        this.wxMonthlyRetainMapper = wxMonthlyRetainMapper;
    }

}
