package com.blaze.common.config;

import com.blaze.common.PostResult;
import com.blaze.data.entity.*;
import com.blaze.data.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 刷新缓存配置：@Configuration 实现自动加载缓存配置，其它注解提供刷新缓存接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 18:46
 */
@Configuration
@CrossOrigin
@RestController
@RequestMapping("/flush")
public class CacheConfig implements ApplicationRunner {

    /**
     * 策略变更时间缓存
     */
    static ConcurrentHashMap<String, Long> MODIFY_TIME_CACHE_MAP = new ConcurrentHashMap<>();
    private WxConfigService wxConfigService;
    private ConfigAdAppService configAdAppService;
    private ConfigAdContentPoolService configAdContentPoolService;
    private ConfigAdPositionService configAdPositionService;
    private ConfigAdContentService configAdContentService;
    private ConfigAdContentInfoService configAdContentInfoService;
    private WxAppConfigService wxAppConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.flushCache();
    }

    @GetMapping("/logic")
    public PostResult flushCache() {
        this.wxConfigService.updateAllCacheEntity(WxConfig.class);
        this.configAdAppService.updateAllCacheObject(ConfigAdApp.class);
        this.configAdContentPoolService.updateAllCacheEntity(ConfigAdContentPool.class);
        this.configAdPositionService.updateAllCacheEntity(ConfigAdPosition.class);
        this.configAdContentService.updateAllCacheEntity(ConfigAdContent.class);
        this.configAdContentInfoService.updateAllCacheEntity(ConfigAdContentInfo.class);
        this.wxAppConfigService.updateAllCacheEntity(WxAppConfig.class);
        MODIFY_TIME_CACHE_MAP.put("time", System.currentTimeMillis());
        return new PostResult();
    }

    @GetMapping("/effectiveTime")
    public PostResult sendModifyTime() {
        PostResult postResult = new PostResult();
        postResult.setData((MODIFY_TIME_CACHE_MAP));
        return postResult;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setConfigAdAppService(ConfigAdAppService configAdAppService) {
        this.configAdAppService = configAdAppService;
    }

    @Autowired
    public void setConfigAdContentPoolService(ConfigAdContentPoolService configAdContentPoolService) {
        this.configAdContentPoolService = configAdContentPoolService;
    }

    @Autowired
    public void setConfigAdPositionService(ConfigAdPositionService configAdPositionService) {
        this.configAdPositionService = configAdPositionService;
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

    @Autowired
    public void setConfigAdContentInfoService(ConfigAdContentInfoService configAdContentInfoService) {
        this.configAdContentInfoService = configAdContentInfoService;
    }

    @Autowired
    public void setWxAppConfigService(WxAppConfigService wxAppConfigService) {
        this.wxAppConfigService = wxAppConfigService;
    }

}
