package com.blazefire.task.ad;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.BaseConfig;
import com.blazefire.dao.second.model.AdValue;
import com.blazefire.service.AdValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 广告日志定时分析：按用户去重分析
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-17 17:06
 */
public class AdUserAnalysisTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdUserAnalysisTask.class);

    private BaseConfig baseConfig;

    private AdValueService adValueService;

    /**
     * 每小时01分开始分析上一小时的日志
     */
    //    @Scheduled(cron = "0 1 0 * * ? ")
    public void beginAnalysis() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String lastDate = dateFormat.format(calendar.getTime());
        // 2、开始分析
        beginAnalysis(lastDate);
    }

    /**
     * 分析指定日期的log
     *
     * @param dateNum 日期，格式：20200317
     */
    public void beginAnalysis(String dateNum) {
        // 读取指定日期的日志压缩包
        File files = new File(this.baseConfig.getAdLogSaveDir());
        if (files.isDirectory()) {
            // 根据logback的规则读取日志
            String[] nameList = files.list((dir, name) -> name.startsWith("data_" + dateNum));
            if (nameList != null) {
                // 所有压缩文件读取的数据最终都汇总到当前List
                List<String> logList = new ArrayList<>();
                for (String logFileName : nameList) {
                    // 从备份的压缩文件中读取日志数据
                    logList.addAll(getLogListFromZip(logFileName));
                }
                analysisLogList(Integer.parseInt(dateNum), logList);
            }
        }
    }

    /**
     * 把压缩文件中的数据逐行读取到list中
     *
     * @param logFileName 压缩文件名称
     * @return 一行行的日志数据
     */
    private List<String> getLogListFromZip(String logFileName) {
        List<String> logList = new ArrayList<>();
        String file = this.baseConfig.getAdLogSaveDir() + File.separator + logFileName;
        try {
            ZipFile zf = new ZipFile(file);
            Enumeration<?> zipEnum = zf.entries();
            while (zipEnum.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) zipEnum.nextElement();
                if (!ze.isDirectory() && ze.getSize() > 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    while ((line = br.readLine()) != null) {
                        logList.add(line);
                    }
                    br.close();
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return logList;
    }

    /**
     * 分析日志数据
     *
     * @param hourNum 前一小时的时间戳字符串
     * @param logList 日志列表
     */
    private void analysisLogList(int hourNum, List<String> logList) {
        // 已经分析过的用户缓存Map，用以去重，key是用户uid，value是一个长度为5的boolean型数组，0~4分别表示当前用户是否统计过展示、点击、中转展示、中转点击、目标展示
        Map<String, boolean[]> userMap = new HashMap<>();
        Map<String, AdValue> adValueMap = new HashMap<String, AdValue>();
        for (String logInfo : logList) {
            // logInfo的格式是：2020-03-09 20:58:24.889 - {"node":[{"spaceId":16,"showIndex":0,"positionId":11,"contentId":15}],
            // "uid":"oxDM75P3ylkAijrrPvJmt_ivrrKA","appId":"wx75f1c4d8cd887fd6","time":1583746762530,"type":"show","version":"2.2.1"}
            logInfo = logInfo.substring(26);
            JSONObject infoObject = JSONObject.parseObject(logInfo);
            JSONArray nodeArray = infoObject.getJSONArray("node");
            for (int i = 0; i < nodeArray.size(); i++) {
                AdValue adValue = new AdValue();
                adValue.setHourNum(hourNum);
                adValue.setAppId(infoObject.getString("appId"));
                adValue.setVersion(infoObject.getString("version"));
                JSONObject nodeObject = nodeArray.getJSONObject(i);
                adValue.setAdPositionId(nodeObject.getInteger("positionId"));
                adValue.setAdSpaceId(nodeObject.getInteger("spaceId"));
                adValue.setAdContentId(nodeObject.getInteger("contentId"));
                adValue.setAdShowIndex(nodeObject.getInteger("showIndex"));
                switch (infoObject.getString("type")) {
                    // 展示
                    case "show":
                        adValue.setShowNum(1);
                        break;
                    // 点击
                    case "click":
                        adValue.setClickNum(1);
                        break;
                    // 中转App展示
                    case "promoteShow":
                        adValue.setPromoteShowNum(1);
                        break;
                    // 中转App点击
                    case "promoteClick":
                        adValue.setPromoteClickNum(1);
                        break;
                    // 目标App展示
                    case "target":
                        adValue.setTargetShowNum(1);
                        break;
                    default:
                        break;
                }
                LOGGER.debug(adValue.toString());
                collectAdValue(adValue, adValueMap);
            }
        }
        this.adValueService.collectSaveAdValue(adValueMap);
    }

    /**
     * 汇总数据
     *
     * @param adValue    新的广告数据
     * @param adValueMap 数据汇总Map
     */
    private void collectAdValue(AdValue adValue, Map<String, AdValue> adValueMap) {
        AdValue tempValue = adValueMap.get(adValue.getKey());
        if (tempValue != null) {
            adValue.setShowNum(adValue.getShowNum() + tempValue.getShowNum());
            adValue.setClickNum(adValue.getClickNum() + tempValue.getClickNum());
            adValue.setPromoteShowNum(adValue.getPromoteShowNum() + tempValue.getPromoteShowNum());
            adValue.setPromoteClickNum(adValue.getPromoteClickNum() + tempValue.getPromoteClickNum());
            adValue.setTargetShowNum(adValue.getTargetShowNum() + tempValue.getTargetShowNum());
        }
        adValueMap.put(adValue.getKey(), adValue);
    }

    @Autowired
    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    @Autowired
    public void setAdValueService(AdValueService adValueService) {
        this.adValueService = adValueService;
    }

}
