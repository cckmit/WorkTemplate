package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxUserPortraitMapper;
import com.blazefire.dao.second.model.WxUserPortrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxUserPortrait Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-08 00:44
 */
@Service
public class WxUserPortraitService {

    private WxUserPortraitMapper wxUserPortraitMapper;

    /**
     * 新增用户画像数据
     *
     * @param wxUserPortrait 用户画像数据
     */
    public void insert(WxUserPortrait wxUserPortrait) {
        if (wxUserPortrait != null) {
            this.wxUserPortraitMapper.delete(wxUserPortrait);
            this.wxUserPortraitMapper.insert(wxUserPortrait);
        }
    }


    @Autowired
    public void setWxUserPortraitMapper(WxUserPortraitMapper wxUserPortraitMapper) {
        this.wxUserPortraitMapper = wxUserPortraitMapper;
    }

}
