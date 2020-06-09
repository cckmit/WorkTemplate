package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.Online;
import com.cc.manager.modules.jj.mapper.OnlineMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
@DS("jj")
public class OnlineService extends BaseStatsService<Online, OnlineMapper> {

    /*** 是否进行对比*/
    private boolean isCompare = false;
    /*** 选择线类型*/
    private String lineType = "online";
    /*** 房间占用数*/
    private static final String BUSY_ROOM = "buzy";
    /*** 在线人数*/
    private static final String ONLINE_NUMBER = "online";
    /*** 空闲房间数*/
    private static final String IDLE_ROOM = "idle";
    /*** ECharts图y轴点数量*/
    private static final Integer Y_AXIS = 144;

    /**
     * @param statsListParam 查询参数
     * @return 查询结果
     */
    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空，并进行初始化
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        lineType = statsListParam.getQueryObject().getString("lineType");
        try {
            QueryWrapper<Online> queryWrapper = new QueryWrapper<>();
            queryWrapper.between(" Date(insertTime)", beginDate, endDate);
            List<Online> data = this.list(queryWrapper);
            Vector<Online> result = new Vector<>();
            //游戏名称
            if (StringUtils.isNotBlank(statsListParam.getQueryObject().getString("gameCode"))) {
                //游戏进行解析
                for (Online online : data) {
                    if (online.getGameInfo() == null) {
                        continue;
                    }
                    JSONObject game = JSONObject.parseObject(online.getGameInfo());
                    if (game == null) {
                        continue;
                    }
                    JSONObject room = game.getJSONObject(statsListParam.getQueryObject().getString("gameCode"));
                    if (room == null) {
                        continue;
                    }
                    Online line = new Online();
                    line.setInsertTime(online.getInsertTime());
                    line.setTimes(online.getTimes());
                    line.setOnline(room.getInteger("total"));
                    line.setBuzyRoom(room.getInteger("room"));
                    result.add(line);
                }
            } else {
                result.addAll(data);
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(result)));
            statsListResult.setTotalRow(null);
            statsListResult.setCount(result.size());
            statsListResult.setLineData(getECharts(data));
        } catch (Exception e) {
            statsListResult.setCode(2);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    /**
     * 获取折线图数据
     *
     * @param data 查找数据
     */
    private JSONObject getECharts(List<Online> data) {
        JSONObject lineData = new JSONObject();
        //xAxis 点数据
        JSONArray xAxis = new JSONArray();
        //点数：144个
        for (int i = 0; i < Y_AXIS; i++) {
            int val = i * 10;
            int x = val % 60, y = val / 60;
            xAxis.add(String.format("%02d:%02d", y, x));
        }
        lineData.put("xAxis", xAxis);
        //series 线集合 {name:string,data:array,smooth:false}
        JSONArray series = new JSONArray();
        //数据点进行换算
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Map<String, int[]> cache = new HashMap<>(16);
        data.forEach(point ->
        {
            String time = format.format(point.getTimes());
            String[] val = time.split(":");
            //获取当前key值
            int key = (Integer.parseInt(val[0]) * 60 + Integer.parseInt(val[1])) / 10;
            String day = dateFormat.format(point.getTimes());
            Integer online = point.getOnline();
            Integer buzy = point.getBuzyRoom();
            Integer idle = point.getIdleRoom();
            putValue(cache, key, day + "-online", online);
            putValue(cache, key, day + "-buzy", buzy);
            putValue(cache, key, day + "-idle", idle);
        });
        cache.forEach((k, v) ->
        {
            JSONObject info = new JSONObject();
            if (isCompare && !k.endsWith(lineType)) {
                return;
            }
            String name = "剩余房间数";
            if (k.endsWith(ONLINE_NUMBER)) {
                name = "在线人数";
            } else if (k.endsWith(BUSY_ROOM)) {
                name = "房间占用数";
            }
            if (isCompare) {
                name = k.replace(lineType, name);
            }
            info.put("name", name);
            info.put("data", v);
            info.put("smooth", k.endsWith(IDLE_ROOM));
            series.add(info);
        });
        if (series.isEmpty()) {
            lineData.put("xAxis", new JSONArray());
        }
        lineData.put("series", series);
        return lineData;
    }

    /**
     * 设置点值
     */
    private void putValue(Map<String, int[]> cache, int key, String day, Integer val) {
        int[] value = cache.get(day);
        if (value == null) {
            value = new int[144];
        }
        value[key] = val != null ? val : 0;
        cache.put(day, value);
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Online> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Online> entityList, StatsListResult statsListResult) {
        return null;
    }

}
