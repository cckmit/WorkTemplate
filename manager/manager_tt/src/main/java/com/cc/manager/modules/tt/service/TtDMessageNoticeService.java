package com.cc.manager.modules.tt.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.tt.config.TtConfig;
import com.cc.manager.modules.tt.entity.TtMessageNotice;
import com.cc.manager.modules.tt.mapper.TtMessageNoticeMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author cf
 * @date 2020-08-06
 */
@Service
@DS("tt")
public class TtDMessageNoticeService extends BaseStatsService<TtMessageNotice, TtMessageNoticeMapper> {

    private TtConfig ttConfig;

    public PostResult getNoticeData(JSONObject jsonObject) {

        String sessionId = jsonObject.getString("sdessionId");
        String messageNoticeUrl = ttConfig.getMessageNoticeUrl();
        String messageNoticeBody = HttpRequest.get(messageNoticeUrl).header("Content-Type", "application/json").cookie("sdessionid=" + sessionId).execute().body();
        JSONObject resObject = JSONObject.parseObject(messageNoticeBody);
        JSONArray data = resObject.getJSONArray("data");
        ArrayList<TtMessageNotice> ttMessageNoticeList = new ArrayList<>();
        for (Object datum : data) {
            TtMessageNotice ttMessageNotice = new TtMessageNotice();
            JSONObject datumObject = JSONObject.parseObject(datum.toString());
            ttMessageNotice.setId(datumObject.getInteger("id"));
            ttMessageNotice.setStatus(datumObject.getInteger("status"));
            ttMessageNotice.setType(datumObject.getString("type"));
            ttMessageNotice.setSendTime(datumObject.getString("send_time"));
            ttMessageNoticeList.add(ttMessageNotice);
        }
        this.saveOrUpdateBatch(ttMessageNoticeList);
        return null;
    }

    @Override
    public boolean saveOrUpdate(TtMessageNotice entity) {
        String sendTime = entity.getSendTime();
        LambdaQueryWrapper<TtMessageNotice> ttMessageNoticeQueryWrapper = new LambdaQueryWrapper<>();
        ttMessageNoticeQueryWrapper.eq(TtMessageNotice::getSendTime, sendTime);
        TtMessageNotice tableContent = this.getOne(ttMessageNoticeQueryWrapper);
        //数据不存在则插入
        if (tableContent == null) {
            return this.save(entity);
        }
        return true;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<TtMessageNotice> entityList, int batchSize) {
        try {
            for (TtMessageNotice ttMessageNotice : entityList) {
                this.saveOrUpdate(ttMessageNotice);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<TtMessageNotice> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<TtMessageNotice> entityList, StatsListResult statsListResult) {
        return null;
    }

    @Autowired
    public void setTtConfig(TtConfig ttConfig) {
        this.ttConfig = ttConfig;
    }

}
