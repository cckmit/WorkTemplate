package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxVisitPageMapper;
import com.blazefire.dao.second.model.WxVisitPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxVisitPage Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 18:15
 */
@Service
public class WxVisitPageService {

    private WxVisitPageMapper wxVisitPageMapper;

    /**
     * @param wxVisitPage 访问页面数据
     */
    public void insert(WxVisitPage wxVisitPage) {
        if (wxVisitPage != null) {
            this.wxVisitPageMapper.delete(wxVisitPage);
            this.wxVisitPageMapper.insert(wxVisitPage);
        }
    }

    @Autowired
    public void setWxVisitPageMapper(WxVisitPageMapper wxVisitPageMapper) {
        this.wxVisitPageMapper = wxVisitPageMapper;
    }

}
