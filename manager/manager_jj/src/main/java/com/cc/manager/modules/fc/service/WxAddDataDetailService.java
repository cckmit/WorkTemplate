package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
public class WxAddDataDetailService extends BaseCrudService<MinitjWx, MinitjWxMapper> {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<MinitjWx> queryWrapper) {

        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("id");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "wx_appid", appId);
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(wx_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        } else {
            String beginTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
            String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            queryWrapper.between("DATE(wx_date)", beginTime, endTime);
        }
        queryWrapper.orderBy(true, false, "wx_date");
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<MinitjWx> entityList) {
        entityList.forEach(minitjWx -> {
            // 通过appID查找配置信息
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, minitjWx.getWxAppid());
            //过滤非wx_config配置里面的数据
            if (wxConfig != null) {
                //fc数据赋值展示数据
                minitjWx.setProgramType(wxConfig.getProgramType());
                minitjWx.setProductName(wxConfig.getProductName());
            } else {
                MiniGame miniGame = this.miniGameService.getCacheEntity(MiniGame.class, minitjWx.getWxAppid());
                if (miniGame != null) {
                    minitjWx.setProgramType(0);
                    minitjWx.setProductName(miniGame.getGameName());
                }
            }
            minitjWx.setAdRevenue(minitjWx.getWxBannerIncome().add(minitjWx.getWxVideoIncome()));
            BigDecimal adRevenue = minitjWx.getAdRevenue();
            Integer wxVideoShow = minitjWx.getWxVideoShow();
            BigDecimal wxVideoIncome = minitjWx.getWxVideoIncome();
            minitjWx.setWxVideoShow(wxVideoShow);
            if (wxVideoShow != 0) {
                minitjWx.setVideoECPM((wxVideoIncome.divide(new BigDecimal(wxVideoShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                minitjWx.setVideoECPM(new BigDecimal(0));
            }
            Integer wxBannerShow = minitjWx.getWxBannerShow();
            BigDecimal wxBannerIncome = minitjWx.getWxBannerIncome();
            if (wxBannerShow != 0) {
                minitjWx.setBannerECPM((wxBannerIncome.divide(new BigDecimal(wxBannerShow), 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(1000)));
            } else {
                minitjWx.setBannerECPM(new BigDecimal(0));
            }
            minitjWx.setRevenueCount(adRevenue);

        });
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<MinitjWx> deleteWrapper) {
        return false;
    }

    /**
     * 查询下拉框选项Json数组，默认是key-value形式的Json对象，如果要自定义，请自行重写
     *
     * @param clazz        查询下拉框数据类
     * @param requestParam 过滤参数
     * @return 下拉框选项数据组
     */
    @Override
    public JSONArray getSelectArray(Class<MinitjWx> clazz, String requestParam) {
        JSONArray selectOptionArray = new JSONArray();
        List<WxConfig> wxConfigEntityList = this.wxConfigService.getCacheEntityList(WxConfig.class);
        List<MiniGame> miniGameEntityList = this.miniGameService.getCacheEntityList(MiniGame.class);

        wxConfigEntityList.forEach(entity -> {
            JSONObject option = new JSONObject();
            option.put("key", entity.getCacheKey());
            option.put("value", entity.getCacheValue());
            selectOptionArray.add(option);
        });
        miniGameEntityList.forEach(entity -> {
            JSONObject option = new JSONObject();
            option.put("key", entity.getCacheKey());
            option.put("value", entity.getCacheValue());
            selectOptionArray.add(option);
        });
        return selectOptionArray;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setMiniGameService(MiniGameService miniGameService) {
        this.miniGameService = miniGameService;
    }

}
