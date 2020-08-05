package com.blazefire.task.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.model.WxVisitPage;
import com.blazefire.service.entity.WxAppAccessToken;
import com.blazefire.service.wx.WxVisitPageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 访问页面
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-31 15:34
 */
@Component
public class WxGetVisitPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxGetVisitPage.class);

    private WxConfig wxConfig;
    private WxVisitPageService wxVisitPageService;

    /**
     * 开始查询数据
     */
    public void getData(List<WxAppAccessToken> wxAppAccessTokenList) {
        LOGGER.info("执行开始……");
        // 获取昨日时间字符串
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());

        for (WxAppAccessToken wxAppAccessToken : wxAppAccessTokenList) {
            // 只查询小程序的分析数据
            if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                this.getData(yesterday, wxAppAccessToken);
            }
        }
        LOGGER.info("执行结束！");
    }

    /**
     * 查询指定App时间段数据，手动查询数据接口
     *
     * @param processObject 查询参数对象
     */
    public JSONObject getData(List<WxAppAccessToken> wxAppAccessTokenList, JSONObject processObject) {
        JSONObject resultObject = new JSONObject();
        String refDate = processObject.getString("refDate");
        if (StringUtils.isBlank(refDate)) {
            resultObject.put("result", "fail");
            resultObject.put("msg", "缺少参数：refDate！");
        } else {
            wxAppAccessTokenList.forEach(wxAppAccessToken -> {
                if (wxAppAccessToken.getType() == 1 && StringUtils.isNotBlank(wxAppAccessToken.getAccessToken())) {
                    this.getData(refDate, wxAppAccessToken);
                }
            });
            resultObject.put("result", "success");
        }
        return resultObject;
    }

    /**
     * 查询微信相关数据
     *
     * @param refDate          查询日期
     * @param wxAppAccessToken 小游戏/小程序信息
     */
    private void getData(String refDate, WxAppAccessToken wxAppAccessToken) {
        String dataUrl = this.wxConfig.getVisitPageUrl() + "?access_token=" + wxAppAccessToken.getAccessToken();
        JSONObject paramObject = new JSONObject();
        paramObject.put("access_token", wxAppAccessToken.getAccessToken());
        paramObject.put("begin_date", refDate);
        paramObject.put("end_date", refDate);
        String dataRes = HttpUtil.post(dataUrl, paramObject.toJSONString());
        if (StringUtils.isNotBlank(dataRes)) {
            LOGGER.debug(wxAppAccessToken.getAppId() + " -> " + dataRes);
            // {"ref_date":"20200331","list":[{"page_path":"pages\/login\/login","page_visit_pv":1660,"page_visit_uv":419,"page_staytime_pv":39.059036,"entrypage_pv":732,"exitpage_pv":529,"page_share_pv":2,"page_share_uv":2},{"page_path":"pages\/index\/index","page_visit_pv":931,"page_visit_uv":446,"page_staytime_pv":1.087003,"entrypage_pv":712,"exitpage_pv":10,"page_share_pv":1,"page_share_uv":1},{"page_path":"pages\/logon\/logon","page_visit_pv":381,"page_visit_uv":257,"page_staytime_pv":5.530184,"entrypage_pv":285,"exitpage_pv":9,"page_share_pv":0,"page_share_uv":0},{"page_path":"pages\/pay\/pay","page_visit_pv":123,"page_visit_uv":56,"page_staytime_pv":1.439024,"entrypage_pv":61,"exitpage_pv":0,"page_share_pv":0,"page_share_uv":0},{"page_path":"pages\/logon\/share","page_visit_pv":78,"page_visit_uv":28,"page_staytime_pv":1.423077,"entrypage_pv":31,"exitpage_pv":0,"page_share_pv":26,"page_share_uv":18},{"page_path":"pages\/turtle\/turtle","page_visit_pv":14,"page_visit_uv":14,"page_staytime_pv":0.928571,"entrypage_pv":14,"exitpage_pv":1,"page_share_pv":0,"page_share_uv":0},{"page_path":"pages\/kefu\/kefu","page_visit_pv":6,"page_visit_uv":3,"page_staytime_pv":1,"entrypage_pv":3,"exitpage_pv":0,"page_share_pv":0,"page_share_uv":0}]}
            JSONObject resObject = JSONObject.parseObject(dataRes);
            if (resObject != null && refDate.equals(resObject.getString("ref_date"))) {
                JSONArray listArray = resObject.getJSONArray("list");
                for (int i = 0; i < listArray.size(); i++) {
                    JSONObject listObject = listArray.getJSONObject(i);
                    WxVisitPage wxVisitPage = new WxVisitPage();
                    wxVisitPage.setAppId(wxAppAccessToken.getAppId());
                    wxVisitPage.setRefDate(resObject.getString("ref_date"));
                    wxVisitPage.setPagePath(listObject.getString("page_path"));
                    wxVisitPage.setPageVisitPv(listObject.getInteger("page_visit_pv"));
                    wxVisitPage.setPageVisitUv(listObject.getInteger("page_visit_uv"));
                    wxVisitPage.setPageStaytimePv(listObject.getDouble("page_staytime_pv"));
                    wxVisitPage.setEntrypagePv(listObject.getInteger("entrypage_pv"));
                    wxVisitPage.setExitpagePv(listObject.getInteger("exitpage_pv"));
                    wxVisitPage.setPageSharePv(listObject.getInteger("page_share_pv"));
                    wxVisitPage.setPageShareUv(listObject.getInteger("page_share_uv"));
                    this.wxVisitPageService.insert(wxVisitPage);
                }
            }
        }
    }

    @Autowired
    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    @Autowired
    public void setWxVisitPageService(WxVisitPageService wxVisitPageService) {
        this.wxVisitPageService = wxVisitPageService;
    }

}
