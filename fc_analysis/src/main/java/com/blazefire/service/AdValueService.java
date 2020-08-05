package com.blazefire.service;

import com.blazefire.dao.second.mapper.AdValueMapper;
import com.blazefire.dao.second.model.AdValue;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-09 22:47
 */
@Service
public class AdValueService {

    private AdValueMapper adValueMapper;

    /**
     * 查询已完成数据分析的最大时间
     *
     * @return 已完成数据分析的最大时间
     */
    public String queryMaxAnalysisHour() {
        return this.adValueMapper.queryMaxAnalysisHour();
    }

    /**
     * 删除一个小时断的分析数据
     *
     * @param hourNum 小时段，格式如：2020042100
     */
    public void deleteOneHourAdValue(String hourNum) {
        this.adValueMapper.deleteOneHourAdValue(Integer.parseInt(hourNum));
    }

    /**
     * 汇总保存广告数据
     *
     * @param adValueMap 数据汇总Map
     */
    public void collectSaveAdValue(Map<String, AdValue> adValueMap) {
        // 将汇总的数据放入list，传递个mybatis执行
        if (!adValueMap.isEmpty()) {
            List<AdValue> adValueCollectList = new ArrayList<>(Lists.newArrayListWithCapacity(adValueMap.size()));
            adValueCollectList.addAll(adValueMap.values());
            this.adValueMapper.insert(adValueCollectList);
        }
    }

    @Autowired
    public void setAdValueMapper(AdValueMapper adValueMapper) {
        this.adValueMapper = adValueMapper;
    }

}
