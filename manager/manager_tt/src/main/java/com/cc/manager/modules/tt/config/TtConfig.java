package com.cc.manager.modules.tt.config;

/**
 * @author: CF
 * @date 2020/7/14
 */

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:module-tt.properties",encoding = "UTF-8")
@Data
public class TtConfig {

    @Value("${wxConfigApi}")
    private String wxConfigApi;

    @Value("${url}")
    private String url;

    @Value("${appIdListUrl}")
    private String appIdListUrl;
    /**
     * ttRequest
     */
    @Value("${ttRequest}")
    private String ttRequest;
    @Value("${ttNewRequest}")
    private String ttNewRequest;
    @Value("${ttActiveRequest}")
    private String ttActiveRequest;

    @Value("${watermelonRequest}")
    private String watermelonRequest;
    @Value("${watermelonNewRequest}")
    private String watermelonNewRequest;
    @Value("${watermelonActiveRequest}")
    private String watermelonActiveRequest;

    @Value("${tikTokRequest}")
    private String tikTokRequest;
    @Value("${tikTokNewRequest}")
    private String tikTokNewRequest;
    @Value("${tikTokActiveRequest}")
    private String tikTokActiveRequest;

    @Value("${ttExtremeRequest}")
    private String ttExtremeRequest;
    @Value("${ttExtremeNewRequest}")
    private String ttExtremeNewRequest;
    @Value("${ttExtremeActiveRequest}")
    private String ttExtremeActiveRequest;

    @Value("${tikTokExtremeRequest}")
    private String tikTokExtremeRequest;
    @Value("${tikTokExtremeNewRequest}")
    private String tikTokExtremeNewRequest;
    @Value("${tikTokExtremeActiveRequest}")
    private String tikTokExtremeActiveRequest;

    @Value("${ttBannerUrl}")
    private String ttBannerUrl;
    @Value("${ttVideoUrl}")
    private String ttVideoUrl;
    @Value("${ttIntUrl}")
    private String ttIntUrl;

    @Value("${ttExtremeVideoUrl}")
    private String ttExtremeVideoUrl;
    @Value("${ttExtremeBannerUrl}")
    private String ttExtremeBannerUrl;

    @Value("${tikTokVideoUrl}")
    private String tikTokVideoUrl;

    @Value("${tikTokExtremeVideoUrl}")
    private String tikTokExtremeVideoUrl;

    @Value("${watermelonVideoUrl}")
    private String watermelonVideoUrl;
    @Value("${watermelonBannerUrl}")
    private String watermelonBannerUrl;

    @Value("${messageNoticeUrl}")
    private String messageNoticeUrl;
    /**
     * excel上传地址
     */
    @Value("${excelSave}")
    private String excelSave;
    @Value("${qqAdRequest}")
    private String qqAdRequest;

    @Value("${qqAdUrl}")
    private String qqAdUrl;
}
