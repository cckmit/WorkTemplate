package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.AppConfig;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.WxConfigMapper;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * WxConfig Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-04 18:06
 */
@Service
@DS("jj")
public class WxConfigService extends BaseCrudService<WxConfig, WxConfigMapper> {

    private AppConfigService appConfigService;
    private PersieServerUtils persieServerUtils;
    private JjConfig jjConfig;

    /**
     * 拼接链接地址
     *
     * @param icon    图标名称
     * @param suffers 拼接数列
     * @return url
     */
    private static String concatUrl(String resultUrl, String icon, String... suffers) {
        if (icon != null) {
            if (suffers != null) {
                for (String suffer : suffers) {
                    resultUrl = resultUrl.concat(suffer).concat("/");
                }
            }
            return resultUrl.concat(icon);
        }
        return null;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxConfig> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
            String appPlatform = queryObject.getString("appPlatform");
            queryWrapper.eq(StringUtils.isNotBlank(appPlatform), "ddAppPlatform", appPlatform);
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<WxConfig> entityList) {
        String url = this.jjConfig.getResHost();
        for (WxConfig config : entityList) {
            String ddShareRes = config.getDdShareRes();
            if (StringUtils.isNotBlank(ddShareRes)) {
                JSONArray shareLists = JSONObject.parseArray(ddShareRes);
                for (int i = 0; i < shareLists.size(); i++) {
                    JSONObject shareList = shareLists.getJSONObject(i);
                    if (shareList != null) {
                        JSONObject jsonObject = JSONObject.parseObject(shareList.toString());
                        String icon = concatUrl(url, jsonObject.getString("url"), config.getId(), "share");
                        if (StringUtils.isNotBlank(icon)) {
                            config.setJumpDirect(icon);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void rebuildSelectedEntity(WxConfig entity) {
        String platformName = "未知";
        switch (entity.getDdAppPlatform()) {
            case "weixin":
                platformName = "微信";
                break;
            case "q":
                platformName = "QQ";
                break;
            case "tt":
                platformName = "头条";
                break;
            default:
                break;
        }

        String programTypeName = "小游戏";
        if (entity.getProgramType() == 1) {
            programTypeName = "小程序";
        } else if (entity.getProgramType() == 2) {
            programTypeName = "公众号";
        }
        entity.setShowName(platformName + "-" + programTypeName + "-" + entity.getProductName() + "-" + entity.getId());

    }

    @Override
    protected void updateInsertEntity(String requestParam, WxConfig entity) {
        AppConfig appConfig = new AppConfig();
        appConfig.setId(entity.getId());
        appConfig.setDdName(entity.getProductName());
        appConfig.setDdProgram(entity.getProgramType());
        appConfig.setDdTime(LocalDateTime.now());
        boolean saveResult = appConfigService.save(appConfig);
        if (saveResult) {
            this.persieServerUtils.refreshTable("app_config");
        }
    }

    @Override
    protected boolean update(String requestParam, WxConfig entity, UpdateWrapper<WxConfig> updateWrapper) {
        AppConfig appConfig = appConfigService.getById(entity.getId());
        appConfig.setDdName(entity.getProductName());
        appConfig.setDdProgram(entity.getProgramType());
        appConfig.setDdTime(LocalDateTime.now());
        boolean updateResult = appConfigService.updateById(appConfig);
        if (updateResult) {
            this.persieServerUtils.refreshTable("app_config");
        }
        return this.updateById(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxConfig> deleteWrapper) {
        if (StringUtils.isNotBlank(requestParam)) {
            List<String> deleteIdList = JSONArray.parseArray(requestParam, String.class);
            if (deleteIdList != null && !deleteIdList.isEmpty()) {
                this.appConfigService.removeByIds(deleteIdList);
                return this.removeByIds(deleteIdList);
            }
        }
        return false;
    }

    /**
     * 刷新配置资源
     *
     * @param parameter 请求参数
     * @return update
     */
    public PostResult refreshResource(JSONArray parameter) {
        PostResult postResult = new PostResult();
        //获取读取路径
        String sharePath = this.jjConfig.getReadRes();
        try {
            for (int i = 0; i < parameter.size(); i++) {
                String appId = parameter.getString(i);
                //进行更新数据
                WxConfig wxConfig = this.getById(appId);
                //拼接分享json路径
                String share = FileUtils.readFileToString(new File(sharePath.concat(appId).concat("/share/readme.json")), "UTF-8");
                if (StringUtils.isNotBlank(share)) {
                    wxConfig.setDdShareRes(share);
                }
                //拼接跳转json路径
                String skip = FileUtils.readFileToString(new File(sharePath.concat(appId).concat("/skip/readme.json")), "UTF-8");
                if (StringUtils.isNotBlank(skip)) {
                    wxConfig.setDdAppSkipRes(skip);
                }
                this.updateById(wxConfig);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
        }
        return postResult;
    }

    @Autowired
    public void setAppConfigService(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
