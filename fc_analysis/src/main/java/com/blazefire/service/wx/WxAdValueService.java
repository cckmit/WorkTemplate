package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxAdValueMapper;
import com.blazefire.dao.second.model.AdValueWx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AdValueWx Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-29 22:44
 */
@Service
public class WxAdValueService {

    private WxAdValueMapper wxAdValueMapper;

    /**
     * 保存微信广告汇总数据
     *
     * @param adValueWxList 微信广告数据列表
     */
    public void insertAdposGeneral(List<AdValueWx> adValueWxList) {
        adValueWxList.forEach(adValueWx -> this.wxAdValueMapper.deleteAdposGeneral(adValueWx));
        this.wxAdValueMapper.insertAdposGeneral(adValueWxList);
    }

    /**
     * 保存微信广告细分数据
     *
     * @param adValueWxList 微信广告数据列表
     */
    public void insertAdunitGeneral(List<AdValueWx> adValueWxList) {
        adValueWxList.forEach(adValueWx -> this.wxAdValueMapper.deleteAdunitGeneral(adValueWx));
        this.wxAdValueMapper.insertAdunitGeneral(adValueWxList);
    }

    @Autowired
    public void setWxAdValueMapper(WxAdValueMapper wxAdValueMapper) {
        this.wxAdValueMapper = wxAdValueMapper;
    }

}
