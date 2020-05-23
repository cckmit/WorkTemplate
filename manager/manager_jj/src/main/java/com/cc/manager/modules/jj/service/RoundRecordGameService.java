package com.cc.manager.modules.jj.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.ExcelUtils;
import com.cc.manager.common.utils.ExportResult;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.Games;
import com.cc.manager.modules.jj.entity.RoundExt;
import com.cc.manager.modules.jj.entity.RoundReceive;
import com.cc.manager.modules.jj.entity.RoundRecord;
import com.cc.manager.modules.jj.mapper.RoundRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cf
 * @since 2020-05-08
 */
@Service
@DS("jj")
public class RoundRecordGameService  extends BaseCrudService<RoundRecord, RoundRecordMapper> {
    private JjConfig jjConfig;
    private GamesService gamesService;
    private RoundExtService roundExtService;
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundRecord> queryWrapper) {
        queryWrapper.eq("ddGroup",0);
        queryWrapper.gt("ddResult",0);
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("times");
            String roundName = queryData.getString("roundName");
            String gameName = queryData.getString("gameName");
            String productName = queryData.getString("productName");
            String ddState = queryData.getString("ddState");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddEnd)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            if (StringUtils.isNotBlank(roundName)) {
                queryWrapper.eq("ddName", roundName);
            }
            if (StringUtils.isNotBlank(ddState)) {
                queryWrapper.eq("ddState", ddState);
            }
        } else {
            String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            queryWrapper.between("DATE(ddEnd)", "2020-01-09", "2020-01-09");

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
                roundRecord.setRoundName(roundName);
                String tip = roundExt.getTip();
                roundRecord.setRoundLength(tip);
            }

        }
    }
    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundRecord> deleteWrapper) {
        return false;
    }


    @Autowired
    public void setGameSetService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setRoundExtService(RoundExtService roundExtService) {
        this.roundExtService = roundExtService;
    }

    public void exportResult(RoundRecord roundRecord, HttpServletResponse response) {
        List<ExportResult> exportResults = new ArrayList<>();
        //获取赛场信息
        Integer ddCode = roundRecord.getDdCode();
        Boolean ddGroup = roundRecord.getDdGroup();
        Integer ddNumber = roundRecord.getDdResult();
        Date ddSubmit = roundRecord.getDdSubmit();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(ddSubmit);
        String roundName = roundRecord.getRoundName();
        String roundLength = roundRecord.getRoundLength();
        int group = 0;
        int num = 0;
        if (ddGroup) {
            group = 1;
        }
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
        exportResults = allResultDeal(allResult, format, roundName, roundLength);

        //表头参数处理
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = formatter.format(roundRecord.getDdSubmit());
        Integer gamesCode = roundRecord.getDdGame();
        Games games = this.gamesService.getCacheEntity(Games.class, gamesCode.toString());
        String gameName = games.getDdName();
        //导出
        ExcelUtils.writeExcel(exportResults, gameTime + "-" + gameName + "-" + roundName, response);
    }

    private List<ExportResult> allResultDeal(JSONArray allResult, String format, String roundName, String roundLength) {
        List<ExportResult> exportResults = new ArrayList<>();
        for (Object object : allResult) {
            //封装每条比赛结果
            ExportResult exportResult = new ExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
            Object value = jsonObject.get("value");
            Object type = jsonObject.get("type");
            Object mark = jsonObject.get("mark");
            int reward = Integer.parseInt(value.toString());
            if ("rmb".equals(type.toString())) {
                reward = reward / 100;
            }
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setIndex(Integer.parseInt(index.toString()));
            exportResult.setName(name.toString());
            exportResult.setUid(uid.toString());
            exportResult.setValue(reward);
            exportResult.setType(type.toString());
            exportResult.setMark(Integer.parseInt(mark.toString()));
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setMatchdate(format);
            //把所有比赛信息放入集合
            exportResults.add(exportResult);
        }
        return exportResults;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
