package com.blazefire.service.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.BaseConfig;
import com.blazefire.config.ProjectServletListener;
import com.blazefire.config.WxConfig;
import com.blazefire.dao.second.mapper.WxNoticeMapper;
import com.blazefire.dao.second.model.WxNotice;
import com.blazefire.service.AppInfoService;
import com.blazefire.service.entity.AppInfo;
import com.blazefire.utils.WxNoticeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-30 21:06
 */
@Service
public class WxNoticeService {

    private WxNoticeMapper wxNoticeMapper;

    private BaseConfig baseConfig;
    private WxConfig wxConfig;
    private AppInfoService appInfoService;

    public void insert(String requestParam) {
        JSONObject jsonObject = JSONObject.parseObject(requestParam);
        if (StringUtils.equals("ok", jsonObject.getString("errmsg"))) {
            String appId = jsonObject.getString("appId");
            JSONArray msgArray = jsonObject.getJSONObject("data").getJSONArray("notify_msg_list");
            long last7DaysSecond = getLast7DaysSecond();

            String appName = "";
            List<AppInfo> appInfoList = this.appInfoService.getWxAppInfoList();
            for (AppInfo appInfo : appInfoList) {
                if (StringUtils.equals(appId, appInfo.getAppId())) {
                    appName = appInfo.getPlatform() + "-" + appInfo.getName();
                    break;
                }
            }
            String finalAppName = appName;

            msgArray.forEach(obj -> {
                JSONObject msgObject = (JSONObject) obj;
                String noticeId = msgObject.getString("id");
                // 判断当前noticeId是否存在
                WxNotice wxNotice = this.wxNoticeMapper.select(noticeId);
                if (wxNotice == null) {
                    wxNotice = new WxNotice();
                    wxNotice.setAppId(appId);
                    wxNotice.setNoticeId(noticeId);
                    wxNotice.setCreateTime(msgObject.getInteger("create_time"));
                    String title = msgObject.getString("title");
                    wxNotice.setTitle(title);
                    for (Object keyword : this.wxConfig.getWxNoticeKeywords()) {
                        if (StringUtils.contains(title, String.valueOf(keyword))) {
                            wxNotice.setMarked(true);
                            if (wxNotice.getCreateTime() > last7DaysSecond) {

                                ProjectServletListener.scheduler.execute(() -> {
                                    WxNoticeUtil wxNoticeUtil = new WxNoticeUtil(this.baseConfig.getWxNoticeUrl(), "微信公告提醒", title, appId + "-" + finalAppName, "小程序公告匹配到关键词标识，请到后台查看");
                                    wxNoticeUtil.send();
                                });
                            }
                            break;
                        }
                    }
                    wxNotice.setContent(msgObject.getString("content"));
                    this.wxNoticeMapper.insert(wxNotice);
                }
            });
        }
    }

    /**
     * 获取7天前秒数
     *
     * @return 7天前秒数
     */
    private long getLast7DaysSecond() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0).plusDays(-7);
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    @Autowired
    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    @Autowired
    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    @Autowired
    public void setWxNoticeMapper(WxNoticeMapper wxNoticeMapper) {
        this.wxNoticeMapper = wxNoticeMapper;
    }

    @Autowired
    public void setAppInfoService(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

}
