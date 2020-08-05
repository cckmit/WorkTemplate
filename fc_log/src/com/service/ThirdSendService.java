package com.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.config.ReadConfig;
import com.tool.CmZip;
import com.tool.Log4j;
import com.work.CmServletListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件转发策略，不进行补发处理,超过内存上限抛出
 *
 * @author xuwei
 */
public class ThirdSendService implements Runnable {
    /**
     * 最大线程数
     */
    private final static long MAX_THREAD = ReadConfig.getLong("MAX_THREAD");

    /**
     * 30s检测一次网络状态
     */
    private final static long MAX_CHECK_TIME = ReadConfig.getLong("MAX_CHECK_TIME");
    /**
     * 一次发送日志上限
     */
    private final static long SEND_LIMIT = ReadConfig.getLong("SEND_LIMIT");

    /**
     * 缓存保存数据
     */
    private final static long CACHE_SAVE_LIMIT = ReadConfig.getLong("CACHE_SAVE_LIMIT");
    /**
     * \
     * 消息队列
     */
    public static Queue<JSONObject> thirdLog = new ConcurrentLinkedQueue<>();

    private int threadIndex;
    /**
     * 是否允许发送
     */
    private static boolean isAllow = true;
    /**
     * 自检时长
     */
    private static long selfCheckTime = 0L;

    private static Logger LOG = LoggerFactory.getLogger(ThirdSendService.class);
    /**
     * 获取当前发送计数
     */
    private static AtomicLong atomicLong = new AtomicLong();

    /**
     * 获取发送关停状态
     */
    private static boolean controlStatus() {
        return ReadConfig.getBoolean("control-status");
    }
    /**
     * 获取当前发送线程状态
     *
     * @return 状态JSON
     */
    public static JSONObject threadStatus() {
        JSONObject data = new JSONObject();
        int queueSize = thirdLog.size();
        data.put("queueSize", queueSize);
        data.put("isAllow", isAllow);
        data.put("isSend", isSend());
        data.put("sendUrl", getSendUrl());
        data.put("controlStatus", controlStatus());
        data.put("selfCheckTime", selfCheckTime);
        data.put("sendSize", atomicLong.get());
        return data;
    }

    private ThirdSendService(int threadIndex) {
        this.threadIndex = threadIndex;
    }

    /**
     * 添加队列
     *
     * @param data 添加元素
     */
    public static void addQueue(JSONObject data) {
        if (!controlStatus()) {
            return;
        }
        //当前大于队列，进行移除
        if (thirdLog.size() > CACHE_SAVE_LIMIT) {
            thirdLog.poll();
        }
        thirdLog.offer(data);
    }

    /**
     * 获取当前发送状态
     *
     * @return 状态
     */
    private static boolean isSend() {
        return ReadConfig.getBoolean("SEND_STATUS");
    }

    /**
     * 发送URL
     *
     * @return URL
     */
    private static String getSendUrl() {
        return ReadConfig.get("THIRD_URL");
    }

    @Override
    public void run() {
        LOG.info("Thread" + threadIndex + "start，surf size：" + thirdLog.size());

        if (!isAllow && (System.currentTimeMillis() - selfCheckTime < MAX_CHECK_TIME)) {
            return;
        }
        isAllow = true;
        selfCheckTime = 0;
        while (true) {
            if (!isSend()) {
                return;
            }
            //保证队列允许发送
            if (!isAllow) {
                break;
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                LOG.error(Log4j.getExceptionInfo(e));
            }
            JSONArray array = new JSONArray();
            long size = SEND_LIMIT;
            //保证队列撑满
            while (size-- > 0) {
                JSONObject data = thirdLog.poll();
                if (data != null) {
                    array.add(data);
                }
            }
            String thirdUrl = getSendUrl();
            int sendSize = array.size();
            //验证队列数据
            if (thirdUrl != null && sendSize > 0) {
                isAllow = sendData(thirdUrl, array);
                if (!isAllow) {
                    selfCheckTime = System.currentTimeMillis();
                    break;
                }
            }
            //检测发送不足，停止补发
            if (sendSize < SEND_LIMIT) {
                break;
            }
        }

    }

    /**
     * 数据发送
     *
     * @param array 数组
     */
    private boolean sendData(String urlParam, JSONArray array) {
        int size = array.size();
        atomicLong.addAndGet(size);
        byte[] buffer = JSONArray.toJSONBytes(array);
        try {
            buffer = CmZip.zipBytes(buffer);
            URL url = new URL(urlParam);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setConnectTimeout(10000);
            httpConn.setReadTimeout(10000);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "close");
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.setRequestProperty("Content-length", String.valueOf(buffer.length));
            OutputStream out = new DataOutputStream(httpConn.getOutputStream());
            out.write(buffer);
            out.flush();
            out.close();
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                byte[] readBytes = new byte[4 * 1024];
                int readed = 0;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((readed = inStream.read(readBytes)) != -1) {
                    byteStream.write(readBytes, 0, readed);
                }
                byte[] b = byteStream.toByteArray();
                LOG.warn("Thread" + threadIndex + "url,size=" + size + ".send finish:" + new String(b, StandardCharsets.UTF_8));
                byteStream.close();
                inStream.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void init() {
        //线程启动
        for (int i = 0; i < MAX_THREAD; i++) {
            CmServletListener.scheduler.scheduleWithFixedDelay(new ThirdSendService(i), 1, 2, TimeUnit.SECONDS);
        }
    }
}
