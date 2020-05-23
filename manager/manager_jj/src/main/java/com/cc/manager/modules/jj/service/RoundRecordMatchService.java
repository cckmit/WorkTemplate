package com.cc.manager.modules.jj.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.ExcelUtils;
import com.cc.manager.common.utils.ExportResult;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.RoundMatchMapper;
import com.cc.manager.modules.jj.mapper.RoundRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
public class RoundRecordMatchService extends BaseCrudService<RoundRecord, RoundRecordMapper> {
    private JjConfig jjConfig;
    private RoundMatchMapper roundMatchMapper;
    private RoundExtService roundExtService;
    private WxConfigService wxConfigService;
    private GamesService gamesService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<RoundRecord> queryWrapper) {
        queryWrapper.eq("ddGroup", 1);
        queryWrapper.gt("ddResult", 0);
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
                queryWrapper.between("DATE(create_time)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            if (StringUtils.isNotBlank(roundName)) {
                queryWrapper.eq("ddName", roundName);
            }
            if (StringUtils.isNotBlank(ddState)) {
                queryWrapper.eq("ddState", ddState);
            }
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
            Integer ddGame = roundRecord.getDdGame();
            String ddRound = roundRecord.getDdRound();
            Integer ddCode = roundRecord.getDdCode();
            RoundMatch roundMatch = roundMatchMapper.selectByPrimaryKey(ddCode);
            //赛制名称
            String roundName = roundMatch.getDdName();
            RoundExt roundExt = this.roundExtService.getCacheEntity(RoundExt.class, ddRound);
            //赛制时长
            String tip = roundExt.getTip();
            String ddAppId = roundMatch.getDdAppId();
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
            //产品名称
            String productName = wxConfig.getProductName();
            Games games = this.gamesService.getCacheEntity(Games.class, ddGame.toString());
            //游戏名称
            String gameName = games.getDdName();
            roundRecord.setDdGroup(true);
            roundRecord.setRoundName(roundName);
            roundRecord.setRoundLength(tip);
            roundRecord.setAppName(productName);
            roundRecord.setAppId(wxConfig.getId());
            roundRecord.setGamesName(gameName);
        }
    }

    public void exportResult(RoundRecord roundRecord, HttpServletResponse response) {
        List<ExportResult> exportResults = new ArrayList<>();
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
        int group = 0;
        int num = 0;
        //判断类型 小程序or小游戏
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
        for (int i = 0; i <= num; i++) {
            //获取比赛结果
            String obtainResultUrl = matchRes + "match-c" + ddCode + "-g" + group + "-i" + ddIndex + "-" + i + ".json";
            String result = HttpUtil.get(obtainResultUrl);
            JSONArray singleResult = JSONArray.parseArray(result);
            allResult.addAll(singleResult);
        }
        //比赛结果处理
        exportResults = allResultDeal(allResult, format, roundName, roundLength);

        Date matchDate = roundRecord.getDdSubmit();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = formatter.format(matchDate);
        String gamesName = roundRecord.getGamesName();
        ExcelUtils.writeExcel(exportResults, gameTime + "-" + gamesName + "-" + roundName, response);
    }

    /**
     * 比赛结果处理
     *
     * @param allResult
     * @param format
     * @param roundName
     * @param roundLength
     * @return
     */
    private List<ExportResult> allResultDeal(JSONArray allResult, String format, String roundName, String roundLength) {
        List<ExportResult> exportResults = new ArrayList<>();
        for (Object object : allResult) {
            ExportResult exportResult = new ExportResult();
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            Object uid = jsonObject.get("uid");
            Object index = jsonObject.get("index");
            Object name = jsonObject.get("name");
            Object ddName = jsonObject.get("ddName");
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
            if (name != null) {
                exportResult.setName(name.toString());
            } else {
                if (ddName != null) {
                    exportResult.setName(ddName.toString());
                } else {
                    exportResult.setName("");
                }
            }
            exportResult.setUid(uid.toString());
            exportResult.setValue(reward);
            exportResult.setType(type.toString());
            exportResult.setMark(Integer.parseInt(mark.toString()));
            exportResult.setRoundName(roundName);
            exportResult.setRoundLength(roundLength);
            exportResult.setMatchdate(format);
            exportResults.add(exportResult);
        }
        return exportResults;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<RoundRecord> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setRoundMatchMapper(RoundMatchMapper roundMatchMapper) {
        this.roundMatchMapper = roundMatchMapper;
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
    public void setGameSetService(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }


}
