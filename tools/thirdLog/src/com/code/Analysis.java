package com.code;


import com.alibaba.fastjson.JSONObject;
import com.tools.db.BatchSQL;
import com.tools.log4j.Log4j;
import com.tools.unzip.UnZipMain;
import com.tools.unzip.ZipFileReadInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xuwei
 */
public class Analysis {
    private ReadLog readLog = new ReadLog();
    private static String logPath = null;
    private static final Logger LOG = LoggerFactory.getLogger("stdoutsLog");
    private static boolean isUpdate = false;

    public static void main(String[] args) {
        logPath = args[0];
        new Analysis().analysis();
    }

    private Analysis() {
    }

    /**
     * 数据分析
     */
    private void analysis() {
        //获取文件夹数据
        File dir = new File(logPath);
        File[] files = dir.listFiles();
        if (!dir.exists() || files == null || files.length <= 0) {
            System.out.println("无需要解析的文件内容");
            return;
        }
        LOG.error("进行解析文件夹：" + files.length);
        for (File file : files) {
            analysis(file);
        }
        LOG.error("進行保存数据库");
        readLog.saveData();
        LOG.error("执行结束！！！");
    }

    /**
     * 分析压缩文件
     *
     * @param zip 压缩文件
     */
    private void analysis(File zip) {
        if (zip.isDirectory()) {
            return;
        }
        try {
            if (zip.getName().endsWith(".zip")) {
                UnZipMain unZipMain = new UnZipMain(readLog);

                unZipMain.display(zip);

            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(zip)));
                String line;
                while ((line = br.readLine()) != null) {
                    readLog.readLogLine(line);
                }
                br.close();
                readLog.readFileFinish();
            }
        } catch (Exception e) {
            Log4j.NAME.EXCEPTION_LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    public static class ReadLog implements ZipFileReadInterface {
        //详情数据
        private List<JSONObject> array = new ArrayList<>();
        private Map<String, JSONObject> billid = new HashMap<>();

        @Override
        public void readLogLine(String line) {
            if (line.trim().isEmpty()) {
                return;
            }
            String time = line.substring(0, 24).trim();
            String data = line.substring(26).trim();
            JSONObject context = JSONObject.parseObject(data);
            String billid = context.getString("billid");
            String[] users = billid.split("#");
            if (users.length > 1) {
                context.put("uid", users[1]);
                context.put("billid", users[0]);
            }
            context.put("serverTime", new Timestamp(context.getLong("serverTime")));
            context.put("ctime", new Timestamp(context.getLong("time")));
            if (context.containsKey("data")) {
                JSONObject values = context.getJSONObject("data");
                context.put("adType", values.getString("adType"));
                if (values.containsKey("position")) {
                    JSONObject position = values.getJSONObject("position");
                    context.put("x", position.get("x"));
                    context.put("y", position.get("y"));
                }
                if (values.containsKey("result")) {
                    context.put("result", values.get("result"));
                }
            }
            context.put("header", time);
            context.put("dataJSON",context.getString("data"));
            context.put("billJSON",billid);
            array.add(context);
        }

        @Override
        public void readFileFinish() {
            if (!isUpdate) {
                isUpdate = true;
                String date = array.get(0).getString("header").substring(0, 10);
                //执行批量删除文件内容
                CmDbSqlResource.instance().execSQLCMDInfo("delete from `fc_logs`.`third_log` where date(`serverTime`)='" + date + "'");
            }
            //执行批量添加
            BatchSQL batch = new BatchSQL() {
                @Override
                public String getSQL() {
                    return "INSERT INTO`fc_logs`.`third_log`(`header`,`appId`,`uid`,`billid`,`serverTime`,`ctime`,`adType`,`status`,`xPostion`,`yPostion`,`result`,`dataJSON`,`billJSON`)" +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }

                @Override
                public int getLength() {
                    return array.size();
                }

                @Override
                public void addBatch(PreparedStatement prest, int index) throws SQLException {
                    JSONObject data = array.get(index);
                    if (data != null) {
                        int i = 0;
                        prest.setObject(++i, data.get("header"));
                        prest.setObject(++i, data.get("appId"));
                        prest.setObject(++i, data.get("uid"));
                        prest.setObject(++i, data.get("billid"));
                        prest.setObject(++i, data.get("serverTime"));
                        prest.setObject(++i, data.get("ctime"));
                        prest.setObject(++i, data.get("adType"));
                        prest.setObject(++i, data.get("type"));
                        prest.setObject(++i, data.get("x"));
                        prest.setObject(++i, data.get("y"));
                        prest.setObject(++i, data.get("result"));
                        prest.setObject(++i, data.get("dataJSON"));
                        prest.setObject(++i, data.get("billJSON"));
                    }
                }
            };
            CmDbSqlResource.instance().execBatchSQL(batch);
            collect();
        }

        private void collect() {
            for (JSONObject context : array) {
                String date = context.getString("header").substring(0, 10);
                String key = date.concat(context.getString("billid"));
                JSONObject values = billid.get(key);
                if (values == null) {
                    values = createJson(date, context.get("billid"), context.get("uid"), context.get("appId"));
                }
                try {
                    String type = context.getString("type");
                    switch (type) {
                        case "start": {
                            values.put("start", 1 + values.getInteger("start"));
                        }
                        break;
                        case "error": {
                            String ad = context.get("adType") + "Error";
                            values.put(ad, 1 + values.getInteger(ad));
                        }
                        break;
                        case "result": {
                            String ad = context.getString("adType");
                            JSONObject adData = context.getJSONObject("data");
                            String result = adData.getString("result");
                            if ("fail".equals(result)) {
                                values.put(ad + "Close", 1 + values.getInteger(ad + "Close"));
                            } else if ("success".equals(result)) {
                                values.put(ad + "Finish", 1 + values.getInteger(ad + "Finish"));
                            }
                        }
                        break;
                        case "click": {
                            String ad = context.getString("adType") + "Click";
                            values.put(ad, 1 + values.getInteger(ad));
                        }
                        break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    System.out.println(context);
                }
                billid.put(key, values);
            }
            array.clear();
        }

        public void saveData() {
            try {
                //先进行文件分析
                saveAnalysis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 构造一份json对象
         *
         * @param date   日期
         * @param billid 订单号
         * @param uid    用户编号
         * @param appId  应用编号
         * @return json对象
         */
        private JSONObject createJson(Object date, Object billid, Object uid, Object appId) {
            JSONObject values = new JSONObject();
            values.put("date", date);
            try {
                values.put("dates", new SimpleDateFormat("yyyy-MM-dd").parse((String) date));
            } catch (Exception e) {
                LOG.error(Log4j.getExceptionInfo(e));
            }
            values.put("billid", billid);
            values.put("uid", uid);
            values.put("appId", appId);
            values.put("start", 0);
            values.put("video", 0);
            values.put("videoError", 0);
            values.put("videoClose", 0);
            values.put("videoFinish", 0);
            values.put("videoClick", 0);
            values.put("screen", 0);
            values.put("screenError", 0);
            values.put("screenFinish", 0);
            values.put("screenClick", 0);
            values.put("bannerReq", 0);
            values.put("bannerError", 0);
            values.put("bannerShow", 0);
            values.put("bannerClick", 0);
            return values;
        }

        private void saveAnalysis() {
            try {
                List<JSONObject> collect = new ArrayList<>();
                billid.forEach((k, v) -> collect.add(v));
                saveAnalysisData(collect);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 数据库保存
         *
         * @param collect
         */
        private void saveAnalysisData(List<JSONObject> collect) {
            if (collect.size() <= 0) {
                return;
            }
            String date = collect.get(0).getString("date");
            //执行批量删除文件内容
            CmDbSqlResource.instance().execSQLCMDInfo("delete from `fc_logs`.`third_analysis` where `dates`='" + date + "'");
            //执行批量添加
            BatchSQL batchSQL = new BatchSQL() {
                @Override
                public String getSQL() {
                    return "INSERT INTO `fc_logs`.`third_analysis`(`dates`,`billid`,`uid`,`appId`,`start`,`video`,`videoError`,`videoClose`,`videoFinish`,`videoClick`,`screen`," +
                            "`screenError`,`screenFinish`,`screenClick`,`bannerReq`,`bannerError`,`bannerShow`,`bannerClick`)" +
                            "VALUES" +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }

                @Override
                public int getLength() {
                    return collect.size();
                }

                @Override
                public void addBatch(PreparedStatement prest, int index) throws SQLException {
                    JSONObject data = collect.get(index);
                    if (data != null) {
                        int i = 0;
                        prest.setObject(++i, data.get("dates"));
                        prest.setObject(++i, data.get("billid"));
                        prest.setObject(++i, data.get("uid"));
                        prest.setObject(++i, data.get("appId"));
                        prest.setObject(++i, data.get("start"));
                        prest.setObject(++i, data.get("video"));
                        prest.setObject(++i, data.get("videoError"));
                        prest.setObject(++i, data.get("videoClose"));
                        prest.setObject(++i, data.get("videoFinish"));
                        prest.setObject(++i, data.get("videoClick"));
                        prest.setObject(++i, data.get("screen"));
                        prest.setObject(++i, data.get("screenError"));
                        prest.setObject(++i, data.get("screenFinish"));
                        prest.setObject(++i, data.get("screenClick"));
                        prest.setObject(++i, data.get("bannerReq"));
                        prest.setObject(++i, data.get("bannerError"));
                        prest.setObject(++i, data.get("bannerShow"));
                        prest.setObject(++i, data.get("bannerClick"));
                    }
                }
            };
            CmDbSqlResource.instance().execBatchSQL(batchSQL);
        }
    }
}
