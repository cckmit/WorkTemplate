import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.FcAnalysisApplication;
import com.blazefire.service.entity.AppInfo;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.AppInfoService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-27 16:43
 */
@SpringBootTest
@ContextConfiguration(classes = FcAnalysisApplication.class)
public class WxDataReAnalysis {

    public static void main(String[] args) {
        String url = "http://103.20.249.243:27000/fc_analysis/wxDataReAnalysis";
        List<String> adList = Lists.newArrayList("ADPOS_GENERAL", "ADUNIT_GENERAL");
        List<String> dataList = Lists.newArrayList("DAILY_RETAIN", "MONTHLY_RETAIN", "WEEKLY_RETAIN", "DAILY_SUMMARY", "DAILY_VISIT_TREND", "MONTHLY_VISIT_TREND", "WEEKLY_VISIT_TREND", "USER_PORTRAIT", "VISIT_DISTRIBUTION", "VISIT_PAGE");
        adList.forEach(type -> {
            JSONObject requestParam = new JSONObject();
            requestParam.put("type", type);
            requestParam.put("startDate", "2020-04-30");
            requestParam.put("endDate", "2020-04-30");
            System.out.println(type + " -> " + HttpUtil.post(url, requestParam.toJSONString()));
        });
        dataList.forEach(type -> {
            JSONObject requestParam = new JSONObject();
            requestParam.put("type", type);
            requestParam.put("refDate", "20200430");
            System.out.println(type + " -> " + HttpUtil.post(url, requestParam.toJSONString()));
        });

        JSONObject selfAdObject = new JSONObject();
        selfAdObject.put("type", "SELF_AD_LOG");
        selfAdObject.put("beginHour", "2020043019");
        selfAdObject.put("endHour", "2020040516");
        System.out.println("SELF_AD_LOG" + " -> " + HttpUtil.post(url, selfAdObject.toJSONString()));
    }

    @Autowired
    private AppInfoService appInfoService;

    @Test
    void updateSource() {
        List<AppInfo> list = this.appInfoService.getWxAppInfoList();
        list.forEach(appInfo -> {
            String sql = "UPDATE persie_value.ad_value set appPlatform = '" + appInfo.getPlatform() + "', appType = " + appInfo.getType() + " where appId = '" + appInfo.getAppId() + "' ;";
            System.out.println(sql);
        });
    }

}
