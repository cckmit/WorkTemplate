package com.blazefire.task.ad;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.BaseConfig;
import com.blazefire.dao.second.model.AdValue;
import com.blazefire.service.AdValueService;
import com.blazefire.service.AppInfoService;
import com.blazefire.service.entity.AppInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 广告日志分析定时任务，每小时01分分析上一小时
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-09 13:22
 */
@Component
public class AdLogAnalysisTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdLogAnalysisTask.class);
    private static final String PRIMARY = "[PRIMARY]";
    private static final String THIRD = "[THIRD]";
    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyyMMddHH"));
    private BaseConfig baseConfig;
    private AppInfoService appInfoService;
    private AdValueService adValueService;

    /**
     * 每小时01分开始分析上一小时的日志
     */
    @Scheduled(cron = "0 6 * * * ? ")
    public void beginAnalysis() {
        LOGGER.info("执行定时任务开始....");

        // 1、查询上一次分析日志的小时断
        String maxAnalysisHour = this.adValueService.queryMaxAnalysisHour();
        if (StringUtils.isBlank(maxAnalysisHour) || StringUtils.equals("0", maxAnalysisHour)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            maxAnalysisHour = DATE_FORMAT_THREAD_LOCAL.get().format(calendar.getTime());
        }

        // 2、获取上一小时的时间字符串
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        String analysisHour = DATE_FORMAT_THREAD_LOCAL.get().format(calendar.getTime());
        LOGGER.info("开始分析" + maxAnalysisHour + "~" + analysisHour);

        // 3、从街机核心服务器下载日志
        String logicPath = downloadLogFile(maxAnalysisHour, analysisHour, "adLog");
        String logic2Path = downloadLogFile(maxAnalysisHour, analysisHour, "adLog2");
        String logic3Path = downloadLogFile(maxAnalysisHour, analysisHour, "adLog3");

        // 4、开始分析日志
        Multimap<String, String> logMap = ArrayListMultimap.create();
        getLogInfoFromZip(logicPath, logMap);
        getLogInfoFromZip(logic2Path, logMap);
        getLogInfoFromZip(logic3Path, logMap);

        analysisLogMap(logMap);
        LOGGER.info("执行定时结束！");
    }

    /**
     * 重新分析某个始段的数据
     *
     * @param requestObject 请求的Json对象
     */
    public JSONObject beginAnalysis(JSONObject requestObject) {
        LOGGER.info("执行手动任务开始....");
        JSONObject resultObject = new JSONObject();
        resultObject.put("result", "fail");

        // 获取并判断数据
        String beginHour = requestObject.getString("beginHour");
        String endHour = requestObject.getString("endHour");
        if (StringUtils.isBlank(beginHour) || StringUtils.isBlank(endHour)) {
            resultObject.put("msg", "【beginHour】和【endHour】为空！");
            return resultObject;
        }
        LOGGER.info("开始分析" + beginHour + "~" + endHour);

        // 3、从街机核心服务器下载日志
        String logicPath = downloadLogFile(beginHour, endHour, "adLog");
        String logic2Path = downloadLogFile(beginHour, endHour, "adLog2");
        String logic3Path = downloadLogFile(beginHour, endHour, "adLog3");

        // 4、开始分析日志
        Multimap<String, String> logMap = ArrayListMultimap.create();
        getLogInfoFromZip(logicPath, logMap);
        getLogInfoFromZip(logic2Path, logMap);
        getLogInfoFromZip(logic3Path, logMap);

        analysisLogMap(logMap);

        resultObject.put("result", "success");
        LOGGER.info("执行手动任务结束！");
        return resultObject;
    }

    /**
     * 去正式环境服务器下载文件
     *
     * @param beginDate 开始时间
     * @param endDate   结束书简
     * @param source    文件源
     * @return 日志地址
     */
    public String downloadLogFile(String beginDate, String endDate, String source) {
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        String param = "fileName=" + currentTimeMillis + source + "&source=" + source + "&type=hour&start=" + beginDate + "&end=" + endDate;
        HttpRequest httpRequest = HttpUtil.createGet(this.baseConfig.getLogDownloadUrl() + "?" + param);
        HttpResponse httpResponse = httpRequest.execute();
        byte[] bodyBytes = httpResponse.bodyBytes();
        String logFilePath = AdLogAnalysisTask.class.getResource("/") + currentTimeMillis + source;
        FileUtil.writeBytes(bodyBytes, logFilePath + ".zip");
        return logFilePath;
    }

    /**
     * 把压缩文件中的数据逐行读取到list中
     *
     * @param logPath 日志压缩文件地址
     * @param logMap  数据存储map
     */
    public void getLogInfoFromZip(String logPath, Multimap<String, String> logMap) {
        try {
            // 先解压文件
            File logFileDir = ZipUtil.unzip(logPath + ".zip", logPath);
            File[] logFileArray = logFileDir.listFiles();
            if (logFileArray != null) {
                for (int i = logFileArray.length - 1; i >= 0; i--) {
                    ZipFile zipFile = new ZipFile(logFileArray[i]);
                    String fileName = zipFile.getName();
                    LOGGER.info("开始解析文件 -> " + fileName);
                    // data_2020041717.0.log.zip 格式的文件名
                    String analysisHour = fileName.substring(fileName.indexOf("data_") + 5,
                            fileName.indexOf("data_") + 15);
                    Enumeration<?> zipEnum = zipFile.entries();
                    while (zipEnum.hasMoreElements()) {
                        ZipEntry ze = (ZipEntry) zipEnum.nextElement();
                        if (!ze.isDirectory() && ze.getSize() > 0) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze)));
                            String line;
                            while ((line = br.readLine()) != null) {
                                logMap.put(analysisHour, line);
                            }
                            br.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            System.out.println(logPath);
            FileUtil.del(logPath);
            FileUtil.del(logPath + ".zip");
        }
    }

    /**
     * 分析日志数据
     *
     * @param logMap 日志数据Map
     */
    private void analysisLogMap(Multimap<String, String> logMap) {
        // 查询全部小程序，并将数据放入map
        List<AppInfo> appInfoList = this.appInfoService.getWxAppInfoList();
        Map<String, AppInfo> appInfoMap = new HashMap<>();
        appInfoList.forEach(appInfo -> appInfoMap.put(appInfo.getAppId(), appInfo));

        Map<String, AdValue> adValueMap = new HashMap<>(Maps.newHashMapWithExpectedSize(logMap.size()));
        for (String analysisHour : logMap.keySet()) {
            Collection<String> logInfoList = logMap.get(analysisHour);
            LOGGER.info("开始分析：" + analysisHour + " -> " + logInfoList.size());
            this.adValueService.deleteOneHourAdValue(analysisHour);
            logInfoList.forEach(logInfo -> {
                try {
                    if (logInfo.contains(PRIMARY)) {
                        int index = logInfo.indexOf(PRIMARY);
                        logInfo = logInfo.substring(index + PRIMARY.length());
                    } else if (logInfo.contains(THIRD)) {
                        int index = logInfo.indexOf(THIRD);
                        logInfo = logInfo.substring(index + THIRD.length());
                    } else {
                        logInfo = logInfo.substring(26);
                    }
                    JSONObject infoObject = JSONObject.parseObject(logInfo);
                    String type = infoObject.getString("type");
                    switch (type) {
                        // 展示
                        case "show":
                            // 点击
                        case "click":
                            // 中转App展示
                        case "promoteShow":
                            // 中转App点击
                        case "promoteClick":
                            // logInfo的格式是：2020-03-09 20:58:24.889 - {"node":[{"spaceId":16,"showIndex":0,"positionId":11,"contentId":15}],
                            // "uid":"oxDM75P3ylkAijrrPvJmt_ivrrKA","appId":"wx75f1c4d8cd887fd6","time":1583746762530,"type":"show","version":"2.2.1"}
                            JSONArray nodeArray = infoObject.getJSONArray("node");
                            for (int i = 0; i < nodeArray.size(); i++) {
                                AdValue adValue = new AdValue();
                                adValue.setHourNum(Integer.parseInt(analysisHour));
                                String appId = infoObject.getString("appId");
                                adValue.setAppId(appId);
                                AppInfo appInfo = appInfoMap.get(appId);
                                adValue.setAppPlatform(appInfo.getPlatform());
                                adValue.setAppType(appInfo.getType());
                                adValue.setVersion(infoObject.getString("version"));

                                JSONObject nodeObject = nodeArray.getJSONObject(i);
                                adValue.setAdPositionId(nodeObject.getInteger("positionId"));
                                adValue.setAdSpaceId(nodeObject.getInteger("spaceId"));
                                adValue.setAdContentId(nodeObject.getInteger("contentId"));
                                adValue.setAdShowIndex(nodeObject.getInteger("showIndex"));
                                adValue.setTypeNum(type);
                                collectAdValue(adValue, adValueMap);
                            }
                            break;
                        // 目标App展示
                        case "target":
                            // {"uid":"opgqq5SWN78v481WhwXx4_ffSMbA","spaceId":14,"showIndex":1,"positionId":9,"appId":"wx3e42960ce21d85c4","contentId":68,"type":"target","version":"2.3.2"}
                            AdValue adValue = new AdValue();
                            adValue.setHourNum(Integer.parseInt(analysisHour));
                            String appId = infoObject.getString("appId");
                            adValue.setAppId(appId);
                            AppInfo appInfo = appInfoMap.get(appId);
                            adValue.setAppPlatform(appInfo.getPlatform());
                            adValue.setAppType(appInfo.getType());
                            adValue.setVersion(infoObject.getString("version"));

                            adValue.setAdPositionId(infoObject.getInteger("positionId"));
                            adValue.setAdSpaceId(infoObject.getInteger("spaceId"));
                            adValue.setAdContentId(infoObject.getInteger("contentId"));
                            adValue.setAdShowIndex(infoObject.getInteger("showIndex"));
                            adValue.setTypeNum(type);
                            collectAdValue(adValue, adValueMap);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    LOGGER.error(logInfo + "\n" + ExceptionUtils.getStackTrace(e));
                }
            });
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
    public void setAppInfoService(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

    @Autowired
    public void setAdValueService(AdValueService adValueService) {
        this.adValueService = adValueService;
    }

}
