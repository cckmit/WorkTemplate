package com.cc.manager.modules.jj.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.RoundRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class RoundRecordMatchService extends BaseStatsService<RoundRecord, RoundRecordMapper> {

    private JjConfig jjConfig;
    private RoundMatchService roundMatchService;
    private RoundExtService roundExtService;
    private WxConfigService wxConfigService;
    private GamesService gamesService;


    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<RoundRecord> queryWrapper, StatsListResult statsListResult) {
        queryWrapper.eq("ddGroup", 1);
        queryWrapper.gt("ddResult", 0);
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            queryWrapper.between("DATE(create_time)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
        }
        String gameName = statsListParam.getQueryObject().getString("gameName");
        queryWrapper.eq(StringUtils.isNotBlank(gameName), "ddGame", gameName);
        String roundName = statsListParam.getQueryObject().getString("roundName");
        queryWrapper.eq(StringUtils.isNotBlank(roundName), "ddRound", roundName);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<RoundRecord> entityList, StatsListResult statsListResult) {
        String appId = statsListParam.getQueryObject().getString("appId");
        List<RoundRecord> newList = new ArrayList<>();
        for (RoundRecord roundRecord : entityList) {
            roundRecord.setDdGroup(true);
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, roundRecord.getDdRound());
            roundRecord.setRoundLength(roundExt.getTip());
            //赛制名称
            RoundMatch roundMatch = this.roundMatchService.getCacheEntity(RoundMatch.class, String.valueOf(roundRecord.getDdCode()));
            if (StringUtils.isNotBlank(appId)) {
                if (!StringUtils.equals(appId, roundMatch.getDdAppId())) {
                    continue;
                }
            }
            roundRecord.setAppId(roundMatch.getDdAppId());
            roundRecord.setAppName(this.wxConfigService.getCacheValue(WxConfig.class, roundMatch.getDdAppId()));
            roundRecord.setRoundName(roundRecord.getDdRound() + "-" + roundMatch.getDdName());
            //产品名称
            roundRecord.setGamesName(this.gamesService.getCacheValue(Games.class, String.valueOf(roundRecord.getDdGame())));
            newList.add(roundRecord);
        }
        entityList.clear();
        entityList.addAll(newList);
        return null;
    }

    /**
     * 小程序比赛结果导出
     *
     * @param roundRecord roundRecord
     * @param response    response
     */
    public void exportResult(RoundRecord roundRecord, HttpServletResponse response) {

        Integer ddCode = roundRecord.getDdCode();
        Boolean ddGroup = roundRecord.getDdGroup();
        Integer ddNumber = roundRecord.getDdResult();
        Date ddSubmit = roundRecord.getDdSubmit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //赛制结束时间
        String format = sdf.format(ddSubmit);
        //赛制名称
        String roundName = roundRecord.getRoundName();
        //赛制时长
        String roundLength = roundRecord.getRoundLength();
        //判断类型 小程序or小游戏
        int group = ddGroup ? 1 : 0, num = 0;
        if (ddNumber != null) {
            int numbers = ddNumber;
            if (numbers <= 100) {
                num = 0;
            } else {
                num = numbers / 100;
            }
        }
        Integer ddIndex = roundRecord.getDdIndex();
        String matchRes = this.jjConfig.getMatchRes();
        JSONArray allResult = new JSONArray();
        for (int i = 0; i <= num; i++) {
            //获取比赛结果
            String obtainResultUrl = matchRes + "match-c" + ddCode + "-g" + group + "-i" + ddIndex + "-" + i + ".json";
            String result = HttpUtil.get(obtainResultUrl);
            JSONArray singleResult = JSONArray.parseArray(result);
            allResult.addAll(singleResult);
        }
        //比赛结果处理
        List<RoundRecordExportResult> exportResults = allResultDeal(allResult, format, roundName, roundLength);
        List<RoundRecordExportResult> rows = CollUtil.newArrayList(exportResults);
        //表头参数处理
        Date matchDate = roundRecord.getDdSubmit();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = formatter.format(matchDate);
        String gamesName = roundRecord.getGamesName();
        //导出
        excelWriterOut(rows, gameTime, gamesName, roundName, response);
    }

    /**
     * 导出比赛结果
     *
     * @param rows      rows
     * @param gameTime  gameTime 赛制时长
     * @param gameName  gameName
     * @param roundName roundName
     * @param response  response
     */
    private void excelWriterOut(List<RoundRecordExportResult> rows, String gameTime, String gameName, String roundName, HttpServletResponse response) {
        try {
            ExcelWriter writer = ExcelUtil.getWriter(true);
            //自定义标题别名
            writer.addHeaderAlias("roundName", "赛制名称");
            writer.addHeaderAlias("roundLength", "时段");
            writer.addHeaderAlias("index", "排名");
            writer.addHeaderAlias("name", "昵称");
            writer.addHeaderAlias("uid", "uid");
            writer.addHeaderAlias("value", "奖励数量");
            writer.addHeaderAlias("type", "奖励类型");
            writer.addHeaderAlias("mark", "得分");
            writer.addHeaderAlias("matchdate", "比赛结束时间");
            // 合并单元格后的标题行，使用默认标题样式
            writer.merge(8, gameTime + "-" + gameName + "-" + roundName + "比赛结果");
            writer.write(rows, true);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String nameDecode = new String((gameTime + "-" + gameName + "-" + roundName + ".xlsx").getBytes("gb2312"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + nameDecode);
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 比赛结果处理
     *
     * @param allResult   allResult
     * @param format      赛制时间
     * @param roundName   赛制名称
     * @param roundLength 赛制时长
     * @return List
     */
    private List<RoundRecordExportResult> allResultDeal(JSONArray allResult, String format, String roundName, String roundLength) {
        List<RoundRecordExportResult> exportResults = new ArrayList<>();
        for (Object object : allResult) {
            RoundRecordExportResult exportResult = new RoundRecordExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
            Object ddName = jsonObject.get("ddName");
            Object value = jsonObject.get("value");
            Object type = jsonObject.get("type");
            Object mark = jsonObject.get("mark");
            if (value != null) {
                int reward = Integer.parseInt(value.toString());
                if ("rmb".equals(type.toString())) {
                    reward = reward / 100;
                }
                exportResult.value = reward;
            }
            exportResult.roundName = roundName;
            exportResult.roundLength = roundLength;
            exportResult.index = Integer.parseInt(index.toString());
            if (name != null) {
                exportResult.name = name.toString();
            } else {
                if (ddName != null) {
                    exportResult.name = ddName.toString();
                } else {
                    exportResult.name = "";
                }
            }
            exportResult.uid = uid.toString();
            exportResult.type = type.toString();
            exportResult.mark = Integer.parseInt(mark.toString());
            exportResult.roundName = roundName;
            exportResult.roundLength = roundLength;
            exportResult.matchdate = format;
            exportResults.add(exportResult);
        }
        return exportResults;
    }

    @Autowired
    public void setRoundMatchService(RoundMatchService roundMatchService) {
        this.roundMatchService = roundMatchService;
    }

    @Autowired
    public void setRoundExtService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setGamesService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }


}
