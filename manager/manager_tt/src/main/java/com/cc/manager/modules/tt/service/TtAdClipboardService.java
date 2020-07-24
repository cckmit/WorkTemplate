package com.cc.manager.modules.tt.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.TtAdClipboard;
import com.cc.manager.modules.tt.entity.WxConfig;
import com.cc.manager.modules.tt.mapper.TtAdClipboardMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 头条剪切板广告数据
 *
 * @author cf
 * @since 2020-07-21
 */
@Service
public class TtAdClipboardService extends BaseStatsService<TtAdClipboard, TtAdClipboardMapper> {

    private TtConfig ttConfig;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<TtAdClipboard> queryWrapper, StatsListResult statsListResult) {
        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        String appId = statsListParam.getQueryObject().getString("appId");
        queryWrapper.ge("date_val", Integer.parseInt(beginDate.replace("-", "").trim()));
        queryWrapper.le("date_val", Integer.parseInt(endDate.replace("-", "").trim()));
        queryWrapper.eq(StringUtils.isNotBlank(appId), "app_id", appId);
        queryWrapper.orderBy(true, false, "date_val");
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<TtAdClipboard> entityList, StatsListResult statsListResult) {
        String wxConfigs = HttpRequest.get(ttConfig.getWxConfigApi()).execute().body();
        HashMap<String, WxConfig> wxConfigHashMap = new HashMap<>(16);
        JSONArray data = JSONObject.parseObject(wxConfigs).getJSONArray("data");
        for (Object datum : data) {
            WxConfig wxConfig = JSONUtil.toBean(datum.toString(), WxConfig.class);
            wxConfigHashMap.put(wxConfig.getId(), wxConfig);
        }
        LinkedHashMap<String, TtAdClipboard> ttAdClipboardMap = new LinkedHashMap<>();

        for (TtAdClipboard ttAdClipboard : entityList) {
            WxConfig wxConfig = wxConfigHashMap.get(ttAdClipboard.getAppId());
            if (wxConfig != null) {
                ttAdClipboard.setProductName(wxConfig.getProductName());
            }
            TtAdClipboard adClipboard = ttAdClipboardMap.get(ttAdClipboard.getDateVal() + "-" + ttAdClipboard.getAppId() + "-" + ttAdClipboard.getVersion() + "-" + ttAdClipboard.getAdType() + "-" + ttAdClipboard.getAdStatus());
            if (adClipboard != null) {
                adClipboard.setCounts(adClipboard.getCounts() + ttAdClipboard.getCounts());
            } else {
                ttAdClipboardMap.put(ttAdClipboard.getDateVal() + "-" + ttAdClipboard.getAppId() + "-" + ttAdClipboard.getVersion() + "-" + ttAdClipboard.getAdType() + "-" + ttAdClipboard.getAdStatus(), ttAdClipboard);
            }
        }
        Collection<TtAdClipboard> values = ttAdClipboardMap.values();
        entityList.clear();
        entityList.addAll(values);
        return null;
    }

    /**
     * 初始化查询起止时间
     *
     * @param statsListParam 请求参数
     */
    private void updateBeginAndEndDate(StatsListParam statsListParam) {
        String beginDate = null, endDate = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginDate = timeRangeArray[0].trim();
            endDate = timeRangeArray[1].trim();
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }
}
