package com.blazefire.task.tt;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.BaseConfig;
import com.blazefire.dao.second.model.TtAdClipboard;
import com.blazefire.service.tt.TtAdClipboardService;
import com.blazefire.task.ad.AdLogAnalysisTask;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 头条剪切板广告分析
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-07-17 16:22
 */
@Component
public class ClipboardAd {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClipboardAd.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private BaseConfig baseConfig;
    private TtAdClipboardService ttAdClipboardService;

    @Scheduled(cron = "0 0 6 * * ? ")
    public void beginAnalysis() {
        LOGGER.info("执行定时任务开始....");
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(-1);
        String yesterday = DATE_FORMAT.format(localDateTime);
        String logPath = downloadLogFile(yesterday + "00", yesterday + "23", "third2");
        List<String> logList = getLogInfoFromZip(logPath);
        List<TtAdClipboard> list = new ArrayList<>(Lists.newArrayListWithCapacity(logList.size()));
        logList.forEach(logInfo -> analysisLogFile(Integer.parseInt(yesterday), logInfo, list));
        this.ttAdClipboardService.insert(this.collect(list));
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
        HttpRequest httpRequest = HttpUtil.createGet(this.baseConfig.getLogDownloadUrl2() + "?" + param);
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
     * @return 数据存储list
     */
    public List<String> getLogInfoFromZip(String logPath) {
        List<String> logList = new ArrayList<>();
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
                    Enumeration<?> zipEnum = zipFile.entries();
                    while (zipEnum.hasMoreElements()) {
                        ZipEntry ze = (ZipEntry) zipEnum.nextElement();
                        if (!ze.isDirectory() && ze.getSize() > 0) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze)));
                            String line;
                            while ((line = br.readLine()) != null) {
                                logList.add(line);
                            }
                            br.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            FileUtil.del(logPath);
            FileUtil.del(logPath + ".zip");
        }
        return logList;
    }

    /**
     * 分析日志
     *
     * @param yesterday 昨日时间值
     * @param logInfo   日志
     * @param list      数据列表
     */
    private void analysisLogFile(int yesterday, String logInfo, List<TtAdClipboard> list) {
        logInfo = StringUtils.substring(logInfo, 26);
        JSONObject logObj = JSON.parseObject(logInfo);
        TtAdClipboard ttAdClipboard = new TtAdClipboard();
        ttAdClipboard.setDateVal(yesterday);
        ttAdClipboard.setAppId(logObj.getString("appId"));
        ttAdClipboard.setVersion(logObj.getString("version"));
        ttAdClipboard.setPlatform(logObj.getString("platform"));
        String adStatus = logObj.getString("type");
        ttAdClipboard.setAdStatus(adStatus);
        if (!StringUtils.equals("start", adStatus)) {
            ttAdClipboard.setAdType(logObj.getJSONObject("data").getString("adType"));
        }
        list.add(ttAdClipboard);
    }

    private List<TtAdClipboard> collect(List<TtAdClipboard> list) {
        Map<String, TtAdClipboard> map = new HashMap<>(16);
        list.forEach(ttAdClipboard -> {
            TtAdClipboard temp = Optional.ofNullable(map.get(ttAdClipboard.getKey())).orElse(ttAdClipboard);
            temp.countsIncrement();
            map.put(ttAdClipboard.getKey(), temp);
        });
        return new ArrayList<>(map.values());
    }

    @Autowired
    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    @Autowired
    public void setTtAdClipboardService(TtAdClipboardService ttAdClipboardService) {
        this.ttAdClipboardService = ttAdClipboardService;
    }

}
