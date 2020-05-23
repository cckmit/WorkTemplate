package com.cc.manager.modules.jj.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.entity.RoundRecord;
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
public class RoundRecordGameService extends BaseCrudService<RoundRecord, RoundRecordMapper> {

    private JjConfig jjConfig;
    private GamesService gamesService;
    private RoundExtService roundExtService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundRecord> queryWrapper) {
        queryWrapper.eq("ddGroup", 0);
        queryWrapper.gt("ddResult", 0);
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddEnd)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String ddGame = queryObject.getString("gameName");
            queryWrapper.eq(StringUtils.isNotBlank(ddGame), "ddGame", ddGame);
            String ddRound = queryObject.getString("roundName");
            queryWrapper.eq(StringUtils.isNotBlank(ddRound), "ddRound", ddRound);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<RoundRecord> entityList) {
        for (RoundRecord roundRecord : entityList) {
            Integer ddCode = roundRecord.getDdGame();
            Games games = this.gamesService.getCacheEntity(Games.class, ddCode.toString());
            if (games != null) {
                String gameName = games.getDdName();
                roundRecord.setGamesName(gameName);
            }
            String ddRound = roundRecord.getDdRound();
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, ddRound);
            if (roundExt != null) {
                String roundName = roundExt.getDdName();
                roundRecord.setRoundName(ddRound + "-" + roundName);
                String tip = roundExt.getTip();
                roundRecord.setRoundLength(tip);
            }
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundRecord> deleteWrapper) {
        return false;
    }

    /**
     * 导出Excel游戏结果
     *
     * @param roundRecord roundRecord
     */
    public void exportResult(RoundRecord roundRecord, HttpServletResponse response) {
        //获取赛场信息
        Integer ddCode = roundRecord.getDdCode();
        Boolean ddGroup = roundRecord.getDdGroup();
        Integer ddNumber = roundRecord.getDdResult();
        Date ddSubmit = roundRecord.getDdSubmit();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(ddSubmit);
        String roundName = roundRecord.getRoundName();
        String roundLength = roundRecord.getRoundLength();
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
        //获取比赛结果信息
        for (int i = 0; i <= num; i++) {
            String obtainResultUrl = matchRes + "match-c" + ddCode + "-g" + group + "-i" + ddIndex + "-" + i + ".json";
            String result = HttpUtil.get(obtainResultUrl);
            JSONArray singleResult = JSONArray.parseArray(result);
            allResult.addAll(singleResult);
        }
        //比赛结果处理
        List<RoundRecordExportResult> exportResults = allResultDeal(allResult, format, roundName, roundLength);
        List<RoundRecordExportResult> rows = CollUtil.newArrayList(exportResults);

        //表头参数处理
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = formatter.format(roundRecord.getDdSubmit());
        Integer gamesCode = roundRecord.getDdGame();
        Games games = this.gamesService.getCacheEntity(Games.class, gamesCode.toString());
        String gameName = games.getDdName();
        //导出
        excelWriterOut(rows, gameTime, gameName, roundName, response);
    }

    /**
     * 写出比赛结果
     *
     * @param rows      rows
     * @param gameTime  gameTime
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
     * @param format      format
     * @param roundName   roundName
     * @param roundLength roundLength
     * @return List
     */
    private List<RoundRecordExportResult> allResultDeal(JSONArray allResult, String format, String roundName, String roundLength) {
        List<RoundRecordExportResult> exportResults = new ArrayList<>();
        for (Object object : allResult) {
            //封装每条比赛结果
            RoundRecordExportResult exportResult = new RoundRecordExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
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
            exportResult.name = name == null ? "" : name.toString();
            exportResult.uid = uid.toString();
            exportResult.type = type.toString();
            exportResult.mark = Integer.parseInt(mark.toString());
            exportResult.roundName = roundName;
            exportResult.roundLength = roundLength;
            exportResult.matchdate = format;
            //把所有比赛信息放入集合
            exportResults.add(exportResult);
        }
        return exportResults;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

    @Autowired
    public void setGameSetService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setRoundExtService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

}
