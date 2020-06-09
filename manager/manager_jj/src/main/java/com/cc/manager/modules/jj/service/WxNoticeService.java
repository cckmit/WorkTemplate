package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.WxNotice;
import com.cc.manager.modules.jj.mapper.WxNoticeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author cf
 * @since 2020-06-05
 */
@Service
@DS("fc")
public class WxNoticeService extends BaseStatsService<WxNotice, WxNoticeMapper> {

    private JjAndFcAppConfigService jjAndFcAppConfigService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<WxNotice> queryWrapper, StatsListResult statsListResult) {
        String appId = statsListParam.getQueryObject().getString("appId");
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId);
        String title = statsListParam.getQueryObject().getString("title");
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        String isMarked = statsListParam.getQueryObject().getString("isMarked");
        queryWrapper.eq(StringUtils.isNotBlank(isMarked), "is_marked", isMarked);
        queryWrapper.orderByDesc("create_time");
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<WxNotice> entityList, StatsListResult statsListResult) {
        LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
        for (WxNotice wxNotice : entityList) {
            JSONObject appObject = getAllAppMap.get(wxNotice.getAppId());
            if (appObject != null) {
                // 设置产品名称
                wxNotice.setAppId(appObject.getString("name"));
            }
            wxNotice.setNoticeTime(new Date(wxNotice.getCreateTime() * 1000L));
        }
        return null;
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
