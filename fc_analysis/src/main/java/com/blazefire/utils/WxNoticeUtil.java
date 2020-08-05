package com.blazefire.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * 微信通知工具
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-11 20:19
 */
public class WxNoticeUtil {

    /**
     * 标题
     */
    private final String title;
    /**
     * 高亮文字
     */
    private final String highTip;
    /**
     * 正常文字
     */
    private final String normalTip;
    /**
     * 正常文字
     */
    private final String garyTip;
    /**
     * 通知地址
     */
    private String noticeUrl;

    public WxNoticeUtil(String noticeUrl, String title, String highTip, String normalTip, String garyTip) {
        this.noticeUrl = noticeUrl;
        this.title = StringUtils.isNotBlank(title) ? URLUtil.encode(title) : "";
        this.highTip = StringUtils.isNotBlank(highTip) ? URLUtil.encode(highTip) : "";
        this.normalTip = StringUtils.isNotBlank(normalTip) ? URLUtil.encode(normalTip) : "";
        this.garyTip = StringUtils.isNotBlank(garyTip) ? URLUtil.encode(garyTip) : "";
    }

    public static void main(String[] args) {
        HttpUtil.get("https://sms2.blazefire.com/smsv2.php?tag=20&t1=%E5%BE%AE%E4%BF%A1%E5%85%AC%E5%91%8A%E6%8F%90%E9%86%92&h1=%E5%B0%8F%E7%A8%8B%E5%BA%8F%E5%85%AC%E5%91%8A%E5%8C%B9%E9%85%8D%E5%88%B0%E5%85%B3%E9%94%AE%E8%AF%8D%E6%A0%87%E8%AF%86%EF%BC%8C%E8%AF%B7%E5%88%B0%E5%90%8E%E5%8F%B0%E6%9F%A5%E7%9C%8B&h2=&h3=", 3000);
    }

    public void send() {
        this.noticeUrl = MessageFormat.format(this.noticeUrl, title, highTip, normalTip, garyTip);
        System.out.println(this.noticeUrl);
        HttpResponse httpResponse = HttpUtil.createGet(this.noticeUrl).execute(true);
        System.out.println(httpResponse.body());
    }

}
