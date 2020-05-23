package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.WxConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Cf
 * @date 2020-05-09
 */
@Service
@DS("jj")
public class WxConfigAddSpaceService extends BaseCrudService<WxConfig, WxConfigMapper> {


    private JjConfig jjConfig;
    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<WxConfig> entityList) {
        String resPath = this.jjConfig.getResHost();
        for (WxConfig config : entityList) {
            String ddAppSkipRes = config.getDdAppSkipRes();
            try {
                if (!JSONObject.isValidObject(ddAppSkipRes)) {
                    continue;
                }
                JSONObject data = JSONObject.parseObject(ddAppSkipRes);
                //解析JSON的list信息
                if (data.containsKey("list")) {
                    JSONArray list = data.getJSONArray("list");
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            if (object.containsKey("state") && !object.getBoolean("state")) {
                                continue;
                            }
                            if (object.containsKey("local") && object.getBoolean("local")) {
                                config.setLocal("本地");
                                continue;
                            }
                            String icon = concatUrl(resPath, object.getString("icon"), config.getId(), "skip");
                            if (StringUtils.isNotBlank(icon)) {
                                Field field = WxConfig.class.getDeclaredField("list" + i );
                                if (field != null) {
                                    field.setAccessible(true);
                                    field.set(config, icon);
                                }
                            }
                        }
                    }
                }
                //解析JSON的banner信息
                if (data.containsKey("banner")) {
                    JSONArray list = data.getJSONArray("banner");
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            if (object.containsKey("state") && !object.getBoolean("state")) {
                                continue;
                            }
                            if (object.containsKey("local") && object.getBoolean("local")) {
                                config.setLocal("本地");
                                continue;
                            }
                            String url = concatUrl(resPath, object.getString("url"), config.getId(), "skip");
                            if (url != null) {
                                Field field = WxConfig.class.getDeclaredField("banner" + i );
                                if (field != null) {
                                    field.setAccessible(true);
                                    field.set(config, url);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxConfig> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryData = JSONObject.parseObject(crudPageParam.getQueryData());
            if (queryData != null) {
                String appId = queryData.getString("id");
                    queryWrapper.eq(StringUtils.isNotBlank(appId),"ddAppId", appId);
            }
        }

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxConfig> deleteWrapper) {
        return false;
    }

    /**
     * 拼接链接地址
     *
     * @param icon    图标名称
     * @param suffers 拼接数列
     * @return url
     */
    public static String concatUrl(String resultUrl, String icon, String... suffers) {
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
    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }
}
