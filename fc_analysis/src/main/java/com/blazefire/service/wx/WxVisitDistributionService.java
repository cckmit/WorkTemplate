package com.blazefire.service.wx;

import com.blazefire.dao.second.mapper.WxVisitDistributionMapper;
import com.blazefire.dao.second.model.WxVisitDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WxVisitDistribution Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 17:58
 */
@Service
public class WxVisitDistributionService {

    private WxVisitDistributionMapper wxVisitDistributionMapper;

    /**
     * 插入用户小程序访问分布数据
     *
     * @param wxVisitDistribution 用户小程序访问分布数据
     */
    public void insert(WxVisitDistribution wxVisitDistribution) {
        if (wxVisitDistribution != null) {
            this.wxVisitDistributionMapper.delete(wxVisitDistribution);
            this.wxVisitDistributionMapper.insert(wxVisitDistribution);
        }
    }

    @Autowired
    public void setWxVisitDistributionMapper(WxVisitDistributionMapper wxVisitDistributionMapper) {
        this.wxVisitDistributionMapper = wxVisitDistributionMapper;
    }

}
