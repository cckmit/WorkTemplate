package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.AppConfigMapper;
import com.fish.dao.second.mapper.WxConfigMapper;
import com.fish.dao.second.model.AppConfig;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 产品信息配置 Service
 * WxConfigService
 *
 * @author
 * @date
 */
@Service
public class WxConfigService implements BaseService<WxConfig> {
    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    AppConfigMapper appConfigMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    WxConfig wxConfig;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

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

    /**
     * 查询所有WxConfig内容
     *
     * @param parameter
     * @return
     */
    @Override
    public List<WxConfig> selectAll(GetParameter parameter) {
        List<WxConfig> wxConfigs = cacheService.getAllWxConfig();
        String url = baseConfig.getResHost();
        for (WxConfig config : wxConfigs) {
            String ddShareRes = config.getDdshareres();
            try {
                if (ddShareRes != null && ddShareRes.length() > 0) {
                    JSONArray shareLists = JSONObject.parseArray(ddShareRes);
                    for (int i = 0; i < shareLists.size(); i++) {
                        JSONObject shareList = shareLists.getJSONObject(i);
                        if (shareList != null) {
                            JSONObject jsonObject = JSONObject.parseObject(shareList.toString());
                            String icon = concatUrl(url, jsonObject.getString("url"), config.getDdappid(), "share");
                            if (icon != null) {
                                config.setJumpDirect(icon);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("查询产品信息失败" + ", 详细信息:{}", e.getMessage());
            }
        }
        return wxConfigs;
    }

    /**
     * 新增WxConfig内容
     *
     * @param record
     * @return
     */
    public int insert(WxConfig record) {
        int insertAppConfig;
        appConfig.setDdappid(record.getDdappid());
        appConfig.setDdname(record.getProductName());
        appConfig.setDdprogram(record.getProgramType());
        appConfig.setDdtime(new Timestamp(System.currentTimeMillis()));
        try {
            insertAppConfig = appConfigMapper.insert(appConfig);
            LOGGER.info("appConfig插入数据" + insertAppConfig);
        } catch (Exception e) {
            LOGGER.error("新增appConfig信息失败" + ", 详细信息:{}", e.getMessage());
        }
        record.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String ddAppSkipRes = record.getDdappskipres();
        if (ddAppSkipRes != null) {
            String minify = ReadJsonUtil.minify(ddAppSkipRes);
            record.setDdappskipres(minify);
        }
        int insertWxconfig = 0;
        try {
            //新增产品信息
            insertWxconfig = wxConfigMapper.insert(record);
        } catch (Exception e) {
            LOGGER.error("新增产品信息失败" + ", 详细信息:{}", e.getMessage());
        }
        return insertWxconfig;
    }

    /**
     * 更新产品信息
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(WxConfig record) {
        //产品名称去重
        int insert;
        appConfig.setDdappid(record.getDdappid());
        appConfig.setDdname(record.getProductName());
        appConfig.setDdprogram(record.getProgramType());
        appConfig.setDdtime(new Timestamp(System.currentTimeMillis()));
        try {
            insert = appConfigMapper.updateByPrimaryKeySelective(appConfig);
            LOGGER.info("appConfig更新数据：" + insert);
        } catch (Exception e) {
            LOGGER.error("修改产品信息失败" + ", 详细信息:{}", e.getMessage());
        }
        String ddAppSkipRes = record.getDdappskipres();
        if (ddAppSkipRes != null) {
            String minify = ReadJsonUtil.minify(ddAppSkipRes);
            record.setDdappskipres(minify);
        }
        return wxConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {
        if (parameter.getOrder() != null) {
            return;
        }
        parameter.setOrder("desc");
        parameter.setSort("ddappid");
    }

    @Override
    public Class<WxConfig> getClassInfo() {
        return WxConfig.class;
    }

    @Override
    public boolean removeIf(WxConfig wxConfig, JSONObject searchData) {
        if (existValueFalse(searchData.getString("appId"), wxConfig.getDdappid())) {
            return true;
        }
        return existValueFalse(searchData.getString("productsName"), wxConfig.getDdappid());
    }

    /**
     * flush picture
     *
     * @param parameter parameter
     * @return update
     */
    public int flushResource(JSONObject parameter) {
        int updateWxConfig = 0;
        JSONArray array = parameter.getJSONArray("appList");
        for (int i = 0; i < array.size(); i++) {
            String appId = array.getString(i);
            //进行更新数据
            //获取读取路径
            String sharePath = baseConfig.getReadRes();
            WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(appId);
            //拼接分享json路径
            String share = XwhTool.readFileString(sharePath.concat(appId).concat("/share/readme.json"));
            if (share != null) {
                if (StringUtils.isNotEmpty(share)) {
                    wxConfig.setDdshareres(share);
                }
            }
            //拼接跳转json路径
            String skip = XwhTool.readFileString(sharePath.concat(appId).concat("/skip/readme.json"));
            if (skip != null) {
                if (StringUtils.isNotEmpty(skip)) {
                    wxConfig.setDdappskipres(skip);
                }
            }
            updateWxConfig += wxConfigMapper.updateByPrimaryKey(wxConfig);
        }
        return updateWxConfig;
    }

}
